package dao

import javax.inject.Inject

import models.Item
import play.api.Logger
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.Cursor
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocument, Macros}

import scala.concurrent._
import scala.util.{Failure, Success, Try}

class ItemDao @Inject()(implicit val ec: ExecutionContext,
                        val reactiveMongoApi: ReactiveMongoApi) {

  def collection: Future[BSONCollection] = reactiveMongoApi.database.map(_.collection("items"))

  implicit val itemFormat = Macros.handler[Item]

  def list(): Future[Either[Throwable, List[Item]]] = {
    Logger.info("Getting Items from the database.")
    Try {
      collection
        .flatMap(_.find(BSONDocument())
          .cursor[Item]()
          .collect[List](
          Int.MaxValue,
          Cursor.FailOnError[List[Item]]()
        ))
    } match {
      case Failure(e) =>
        Logger.error("An error occurred when getting Items from the database.")
        Future {
          Left(e)
        }
      case Success(itemsF) =>
        itemsF.map { items =>
          Logger.info(s"Getting ${items.size} Items from the database.")
          Right(items)
        }
    }
  }

  def save(item: Item): Future[Either[Throwable, Item]] = {
    Logger.info(s"Save $item to the database.")
    Try {
      collection.map(_.insert(item))
    } match {
      case Failure(e) =>
        Logger.error(s"An error occurred when saving $item to the database.")
        Future {
          Left(e)
        }
      case Success(savedItem) =>
        Logger.info(s"An $item saved to the database.")
        savedItem.map(_ => Right(item))
    }
  }
}
