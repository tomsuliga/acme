SET FOREIGN_KEY_CHECKS=0;
BEGIN WORK;
insert into BG_PLAYER (id, name, turing_type) values (1, 'Tom', 'HUMAN');
insert into BG_PLAYER (id, name, turing_type) values (2, 'HAL 9000', 'COMPUTER');
insert into BG_TURN (id, player_side, dice_id, game_id) values (1,1,1,1);
insert into BG_TURN (id, player_side, dice_id, game_id) values (2,2,2,1);
insert into BG_TURN (id, player_side, dice_id, game_id) values (3,1,3,1);
insert into BG_TURN (id, player_side, dice_id, game_id) values (4,2,4,1);
insert into BG_TURN (id, player_side, dice_id, game_id) values (5,1,5,1);
insert into BG_TURN (id, player_side, dice_id, game_id) values (6,2,6,1);
insert into BG_TURN (id, player_side, dice_id, game_id) values (7,1,7,1);
insert into BG_TURN (id, player_side, dice_id, game_id) values (8,2,8,1);
insert into BG_TURN (id, player_side, dice_id, game_id) values (9,1,9,1);
insert into BG_TURN (id, player_side, dice_id, game_id) values (10,2,10,1);
insert into BG_TURN (id, player_side, dice_id, game_id) values (11,1,11,1);
insert into BG_DICE (id, die1, die2, used1, used2, used3, used4) values (1,6,1,true,true,false,false);
insert into BG_DICE (id, die1, die2, used1, used2, used3, used4) values (2,6,4,true,true,false,false);
insert into BG_DICE (id, die1, die2, used1, used2, used3, used4) values (3,6,4,false,true,false,false);
insert into BG_DICE (id, die1, die2, used1, used2, used3, used4) values (4,4,2,true,true,false,false);
insert into BG_DICE (id, die1, die2, used1, used2, used3, used4) values (5,6,2,false,false,false,false);
insert into BG_DICE (id, die1, die2, used1, used2, used3, used4) values (6,6,5,true,true,false,false);
insert into BG_DICE (id, die1, die2, used1, used2, used3, used4) values (7,2,5,false,true,false,false);
insert into BG_DICE (id, die1, die2, used1, used2, used3, used4) values (8,1,4,true,true,false,false);
insert into BG_DICE (id, die1, die2, used1, used2, used3, used4) values (9,3,1,true,true,false,false);
insert into BG_DICE (id, die1, die2, used1, used2, used3, used4) values (10,1,6,true,true,false,false);
insert into BG_DICE (id, die1, die2, used1, used2, used3, used4) values (11,6,5,true,false,false,false);
insert into BG_MOVE (id, from_point, to_point, turn_id) values (1,0,6,1);
insert into BG_MOVE (id, from_point, to_point, turn_id) values (2,0,1,1);
insert into BG_MOVE (id, from_point, to_point, turn_id) values (3,12,6,2);
insert into BG_MOVE (id, from_point, to_point, turn_id) values (4,5,1,2);
insert into BG_MOVE (id, from_point, to_point, turn_id) values (5,-99,3,3);
insert into BG_MOVE (id, from_point, to_point, turn_id) values (6,5,1,4);
insert into BG_MOVE (id, from_point, to_point, turn_id) values (7,5,3,4);
insert into BG_MOVE (id, from_point, to_point, turn_id) values (8,12,6,6);
insert into BG_MOVE (id, from_point, to_point, turn_id) values (9,12,7,6);
insert into BG_MOVE (id, from_point, to_point, turn_id) values (10,-99,4,7);
insert into BG_MOVE (id, from_point, to_point, turn_id) values (11,5,4,8);
insert into BG_MOVE (id, from_point, to_point, turn_id) values (12,7,3,8);
insert into BG_MOVE (id, from_point, to_point, turn_id) values (13,-99,2,9);
insert into BG_MOVE (id, from_point, to_point, turn_id) values (14,-99,0,9);
insert into BG_MOVE (id, from_point, to_point, turn_id) values (15,6,5,10);
insert into BG_MOVE (id, from_point, to_point, turn_id) values (16,12,6,10);
insert into BG_MOVE (id, from_point, to_point, turn_id) values (17,2,8,11);
insert into BG_GAME (id, game_over, player1_id, player2_id) values (1, false, 1, 2);
COMMIT;
SET FOREIGN_KEY_CHECKS=1;