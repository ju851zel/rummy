package de.htwg.se.rummy

import com.google.inject.AbstractModule
import de.htwg.se.rummy.controller.ControllerInterface
import de.htwg.se.rummy.controller.component.Controller
import de.htwg.se.rummy.model.fileIO.FileIOInterface
import net.codingwell.scalaguice.ScalaModule

class RummyModule extends AbstractModule with ScalaModule {


  def configure() = {
    //    bind[DeskInterface].to[Desk]
    bind[ControllerInterface].to[Controller]
    //    bind[TileInterface].to[Tile]
    //    bind[BoardInterface].to[Board]
    //    bind[PlayerInterface].to[Player]
    bind[FileIOInterface].to[model.fileIO.xml.FileIO]
    //    bind(new TypeLiteral[Set[PlayerInterface]] {}).to(classOf[Player])


  }

}