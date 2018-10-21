package models

import javax.inject.Inject

import scala.concurrent.{ ExecutionContext, Future }

import play.api.libs.json.{ Json, JsObject }

import reactivemongo.bson.{ BSONDocument, BSONObjectID }

import reactivemongo.api.{ Cursor, ReadPreference }
import reactivemongo.api.commands.WriteResult

import reactivemongo.play.json._
import reactivemongo.play.json.collection.JSONCollection

import play.modules.reactivemongo.ReactiveMongoApi


case class Lunchroom(_id: Option[String], name: String, numlunch: Int, openTime:String, closeTime:String, building:String)

object JsonFormats{
  import play.api.libs.json._

  implicit val LunchroomFormat: OFormat[Lunchroom] = Json.format[Lunchroom]
}

class LunchroomRepository @Inject()(implicit ec: ExecutionContext, reactiveMongoApi: ReactiveMongoApi){

  import JsonFormats._

  def lunchroomCollection: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection("lunchroom"))

  def getAll(limit: Int = 100): Future[Seq[Lunchroom]] =
    lunchroomCollection.flatMap(_.find(
      selector = Json.obj(/* Using Play JSON */),
      projection = Option.empty[JsObject])
      .cursor[Lunchroom](ReadPreference.primary)
      .collect[Seq](limit, Cursor.FailOnError[Seq[Lunchroom]]())
    )

  def getLunchroom(id: String): Future[Option[Lunchroom]] =
    lunchroomCollection.flatMap(_.find(
      selector = BSONDocument("_id" -> id),
      projection = Option.empty[BSONDocument])
      .one[Lunchroom])

  def addLunchroom(lunchroom: Lunchroom): Future[WriteResult] = {
    var newLunchRoom = BSONDocument(
          "_id" -> lunchroom._id.getOrElse(BSONObjectID.generate().stringify),
          "name" -> lunchroom.name,
          "numlunch" -> lunchroom.numlunch,
          "openTime" -> lunchroom.openTime,
          "closeTime" -> lunchroom.closeTime,
          "building" -> lunchroom.building
    )
    
    lunchroomCollection.flatMap(_.insert(newLunchRoom))

    }

  def updateLunchroom(id: String, lunchroom: Lunchroom): Future[Option[Lunchroom]] = {
    val selector = BSONDocument("_id" -> id)
    val updateModifier = BSONDocument(
      f"$$set" -> BSONDocument(
        "name" -> lunchroom.name,
        "numlunch" -> lunchroom.numlunch,
        "openTime" -> lunchroom.openTime,
        "closeTime" -> lunchroom.closeTime,
        "building" -> lunchroom.building)
    )

    lunchroomCollection.flatMap(
      _.findAndUpdate(selector, updateModifier, fetchNewObject = true)
        .map(_.result[Lunchroom])
    )
  }

  def deleteLunchroom(id: String): Future[Option[Lunchroom]] = {
    val selector = BSONDocument("_id" -> id)
    lunchroomCollection.flatMap(_.findAndRemove(selector).map(_.result[Lunchroom]))
  }

}
