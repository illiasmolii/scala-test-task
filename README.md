## Run the application

```
$ sbt -Dhttp.port=80 start
```

## Available endpoints

### `POST /log`

```javascript
{
  "name": "John Doe",
  "message": "Hello!"
}
```

### `GET /log`

```javascript
[
  {
    "name": "Jane Doe",
    "message": "Howdy!"
    "timestamp": "2017-05-30T20:18:43.847Z"
  },
  {
    "name": "John Doe",
    "message": "Hello!"
    "timestamp": "2017-05-30T20:18:17.163Z"
  }
]
```