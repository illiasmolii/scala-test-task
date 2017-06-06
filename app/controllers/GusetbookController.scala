package controllers

import javax.inject._

import dao.ItemDao
import models.Item
import play.api.Logger
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class GusetbookController @Inject()(implicit val ec: ExecutionContext,
                                    val dao: ItemDao) extends Controller {

  def get = Action.async {
    Logger.info("GET /log")
    dao.list().map {
      case Right(items: List[Item]) =>
        Logger.info(s"Found ${items.size} items.")
        Ok(Json.toJson(items))
      case Left(e: Throwable) =>
        Logger.error("An error occurred when getting items")
        InternalServerError(e.getMessage)
    }
  }

  def post = Action.async(parse.json[Item]) { implicit request =>
    Logger.info("POST /log")
    val item = request.body
    dao.save(item).map {
      case Right(item: Item) =>
        Logger.info(s"An $item has been saved.")
        Ok(Json.toJson(item))
      case Left(e: Throwable) =>
        Logger.error(s"Internal server error occurred when saving an Item $item")
        InternalServerError(e.getMessage)
    }
  }
}
