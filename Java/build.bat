::@echo off
cls
javac Game.java View.java Controller.java Model.java Tile.java Link.java Pot.java Boomerang.java Sprite.java Json.java
if %errorlevel% neq 0 (
	echo There was an error; exiting now.	
) else (
	echo Compiled correctly!  Running Game...
	java Game	
)

