package de.htwg.se.rummy.controller.component

object AnswerState extends Enumeration {
  val TAKE_TILE, UNDO_TAKE_TILE, BAG_IS_EMPTY, NONE, P_WON,
  TABLE_NOT_CORRECT, P_FINISHED, P_FINISHED_UNDO, CANT_MOVE_THIS_TILE,
  MOVED_TILE, UNDO_MOVED_TILE, P_DOES_NOT_OWN_TILE, UNDO_LAY_DOWN_TILE, PUT_TILE_DOWN
  , ADDED_PLAYER, REMOVED_PLAYER, CREATED_DESK, INSERTING_NAMES_FINISHED,
  ENOUGH_PLAYER, NOT_ENOUGH_PLAYERS, STORED_FILE, LOADED_FILE, COULD_NOT_LOAD_FILE, UNDO_MOVED_TILE_NOT_DONE = Value

}
