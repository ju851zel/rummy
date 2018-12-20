package model.component

import com.google.inject.Inject
import com.google.inject.name.Named
import model._
import model.component.component._
import model.component.component.component.{Color, State}

import scala.collection.SortedSet
import scala.util.Random

case class Desk @Inject()(@Named("Default") players: Set[PlayerInterface], bagOfTiles: Set[TileInterface], sets: Set[SortedSet[TileInterface]]) extends DeskInterface {
  val minSize = 3

  /*takeUpTile fully tested*/
  override def takeUpTile(p: PlayerInterface, t: TileInterface): Desk = removeFromTable(t).addToPlayer(p, t)

  /*removeFromTable fully tested*/
  override def removeFromTable(t: TileInterface): Desk = copy(sets = sets - sets.find(_.contains(t)).get + (sets.find(_.contains(t)).get - t))

  /*putDownTile fully tested*/
  override def putDownTile(p: PlayerInterface, t: TileInterface): Desk = addToTable(t).removeFromPlayer(p, t)

  /*addToTable fully tested*/
  private[model] def addToTable(t: TileInterface): Desk = copy(sets = sets + (SortedSet[TileInterface]() + t))
  /*amountOfPlayers fully tested*/
  override def amountOfPlayers: Int = players.size
  /*checkTable fully tested*/
  override def checkTable(): Boolean = sets.forall(set => checkStreet(set) || checkPair(set))
  /*checkStreet fully tested*/
  private[model] def checkStreet(set: SortedSet[TileInterface]): Boolean = {
    if (set.isEmpty || set.size < minSize) return false
    val x = set.toArray
    var first = x.apply(0)
    for (i <- x.indices) {
      if (i != x.length - 1) {
        if (first.getValue != x.apply(i + 1).getValue - 1 || first.getColor != x.apply(i + 1).getColor) return false
        first = x.apply(i + 1)
      } else {
        if (first.getValue != x.apply(i - 1).getValue + 1 || first.getColor != x.apply(i - 1).getColor) return false
      }
    }
    true
  }
  /*checkPair fully tested*/
  private[model] def checkPair(set: SortedSet[TileInterface]): Boolean = {
    if (set.isEmpty || set.size < minSize || set.size > 4) {
      return false
    }
    var setOfValues = Set[Int]()
    var setOfColors = Set[Color.Value]()
    set.foreach(t => setOfValues += t.getValue)
    set.foreach(t => setOfColors += t.getColor)
    if (setOfValues.size != 1 || setOfColors.size != set.size) return false
    true
  }
  /*previousP fully tested*/
  override def previousP: PlayerInterface = if (currentP.getNumber - 1 < 0) players.find(_.getNumber == players.size - 1).get else players.find(_.getNumber == currentP.getNumber - 1).get
  /*nextP fully tested*/
  override def nextP: PlayerInterface = if (currentP.getNumber + 1 == players.size) players.find(_.getNumber == 0).get else players.find(_.getNumber == currentP.getNumber + 1).get
  /*moveTwoTilesOnDesk fully tested*/
  override def moveTwoTilesOnDesk(t1: TileInterface, t2: TileInterface): Desk = if (setsContains(t1) && setsContains(t2)) copy(sets = (sets - sets.find(_.contains(t1)).get + (sets.find(_.contains(t1)).get - t1) - sets.find(_.contains(t2)).get + (sets.find(_.contains(t2)).get + t1)).filter(_.nonEmpty)) else this
  /*setsContains fully tested*/
  override def setsContains(t: TileInterface): Boolean = sets.exists(_.contains(t))
  /*randomTileInBag fully tested*/
  override def randomTileInBag: TileInterface = bagOfTiles.toVector(Random.nextInt(bagOfTiles.size))
  /*addPlayer fully tested*/
  override def addPlayer(p: PlayerInterface): Desk = copy(players = players + p)
  override def switchToNextPlayer(curr: PlayerInterface, next: PlayerInterface): Desk = {
    val newPlayers = players - curr + players.find(_ == curr).get.changeState(State.WAIT)
    copy(players = newPlayers - next + newPlayers.find(_ == next).get.changeState(State.TURN))
  }
  /*removePlayer fully tested*/
  override def removePlayer(p: PlayerInterface): Desk = copy(players - players.find(_.getNumber == p.getNumber).get)
  /*takeTileFromBagToPlayer fully tested*/
  override def takeTileFromBagToPlayer(p: PlayerInterface, t: TileInterface): Desk = addToPlayer(p, t).removeFromBag(t)
  /*removeFromBag fully tested*/
  private[model] def removeFromBag(t: TileInterface): Desk = copy(bagOfTiles = bagOfTiles - t)
  /*addToPlayer fully tested*/
  private[model] def addToPlayer(p: PlayerInterface, t: TileInterface): Desk = copy(players - p + (players.find(_ == p).get + t))
  /*takeTileFromPlayerToBag fully tested*/
  override def takeTileFromPlayerToBag(p: PlayerInterface, t: TileInterface): Desk = removeFromPlayer(p, t).addToBag(t)
  /*removeFromPlayer fully tested*/
  private[model] def removeFromPlayer(p: PlayerInterface, t: TileInterface): Desk = copy(players = players - p + (players.find(_ == p).get - t))
  /*addToBag fully tested*/
  private[model] def addToBag(t: TileInterface): Desk = copy(bagOfTiles = bagOfTiles + t)
  /*lessThan4P fully tested*/
  override def lessThan4P: Boolean = players.size < 4
  /*correctAmountOfPlayers fully tested*/
  override def correctAmountOfPlayers: Boolean = moreThan1P && players.size <= 4
  /*moreThan1P fully tested*/
  private[model] def moreThan1P: Boolean = players.size >= 2
  /*currentPlayerWon fully tested*/
  override def currentPlayerWon(): Boolean = currentP.won()
  override def viewOfBoard: SortedSet[TileInterface] = currentP.getTiles
  /*currentP fully tested*/
  override def currentP: PlayerInterface = players.find(_.getState == State.TURN).get
  override def viewOfSet: Set[SortedSet[TileInterface]] = sets
}