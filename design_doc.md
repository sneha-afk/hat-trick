## Game Proposal ##

I want to make a clone of the game Tricky Kick where each level is randomized (following certain "blueprints" where blocks are placed). After a certain amount of levels, a "season" is complete and you earn a certain amount of coins. The coins can be spent in a shop: ball colors, different music, etc. It's meant to be a simple, time-killer type of game with simple controls.

Game Controls:

+ Use the A-D keys or LEFT-RIGHT arrow keys to move the ball horizontally.
+ Mouse clicks toggle the ball rolling forward

Game Elements:

+ Save and load game files
+ Levels randomly chosen from 10 files
+ Ball - 2D image that moves across the screen
	+ Controlled with both the mouse and keys
	+ Keys to move left or right
	+ Mouse to start/stop moving forward
+ Death blocks - level ends if the ball rolls into these
	+ Some stay still while others move
+ Goal post - roll the ball into the goal post to complete the level
+ The shop - buy colors to change the color of the ball
+ Customize the color of the ball
+ Change the volume of the music and other sound effects
+ Earn coins after completing one season (seven levels)

How to Win:

+ Beat levels until a season is finished and earn coins
+ Unlimited amount of levels: replay value

## Link Examples ##

+ [Tricky Kick](http://www.freewebarcade.com/game/tricky-kick/)

## Class Design and Brainstorm ##

+ Custom class that extends Stage for the game to be shown in
	+ Easier to switch through different levels like a TV switching channels
	+ Each level can be created in a new Scene and the stage will be set to it
	+ First Scene created in the constructor to show the main menu
+ A Level class extending World
	+ Picks a random blueprint to follow to construct the level (text files determining the position of elements)
		+ Perhaps I can use the LifeGUI project when designing the blueprints: X, O system is helpful and I don't want to make typos if I try hard-typing it
	+ A Node does not have access to the Stage it is in, only the Scene, so I'll make an accompanying LevelViewer that extends Scene. The LevelViewer will keep track of what Game (Stage object) called it so nextLevel() in Game can be called.
+ Need a way keep track of the player's data: PlayerData
	+ Create an object that represents a player's data
		+ Easier to access parts of this data object rather than have it in separate labels and arrays
	+ Can import/export a text file containing a save(helper methods)
		+ Idea: A visual of "save slots" so that the actual files are not accessed by the user
		+ Toggle buttons for now, might upgrade later
	+ Data to store:
		1. Current amount of coins
		2. Current ball skin, music, etc
		3. Number of levels/seasons completed 
		4. Items the player has (bought from the shop)
		5. Items that are still left in the shop
		6. Currently used ball sprite
+ An Item class represents a Item that the player can buy
	+ Stored within a Shop object
	+ Name, image, and price are put into a ItemViewer