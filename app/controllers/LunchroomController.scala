package controllers

import javax.inject.Inject

import models.JsonFormats._
import models.{Lunchroom, LunchroomRepository}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import reactivemongo.bson.BSONObjectID

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class LunchroomController @Inject()(cc: ControllerComponents, lunchroomRepo: LunchroomRepository) extends AbstractController(cc) {
  
  def getAllLunchrooms = Action.async {
    lunchroomRepo.getAll().map { lunchroom =>
      Ok(Json.toJson(lunchroom))
    }
  }

  def getLunchroom(lunchroomId: BSONObjectID) = Action.async {
    lunchroomRepo.getLunchroom(lunchroomId).map { maybeLunchroom =>
      maybeLunchroom.map { lunchroom =>
        Ok(Json.toJson(lunchroom))
      }.getOrElse(NotFound)
    }
  }

  def createLunchroom() = Action.async(parse.json) {
    _.body.validate[Lunchroom].map { lunchroom =>
      lunchroomRepo.addLunchroom(lunchroom).map { _ =>
        Created
      }
    }.getOrElse(Future.successful(BadRequest("Invalid Lunchroom format")))
  }

 
  def updateLunchroom(lunchroomId: BSONObjectID) = Action.async(parse.json){ req =>
    req.body.validate[Lunchroom].map { lunchroom =>
      lunchroomRepo.updateLunchroom(lunchroomId, lunchroom).map {
        case Some(lunchroom) => Ok(Json.toJson(lunchroom))
        case _ => NotFound
      }
    }.getOrElse(Future.successful(BadRequest("Invalid Json")))
  }

  def deleteLunchroom(lunchroomId: BSONObjectID) = Action.async { req =>
    lunchroomRepo.deleteLunchroom(lunchroomId).map {
      case Some(lunchroom) => Ok(Json.toJson(lunchroom))
      case _ => NotFound
    }
  }
}
