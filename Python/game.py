# Name: Vincent Hassman
# Date: 4 May 2023
# Description: Re-implements Assignment 5 in the Python programming language 
import pygame
import time

from pygame.locals import*
from time import sleep

class Sprite():
	def __init__(self, x, y, width, height, image):
		self.x = x
		self.y = y
		self.width = width
		self.height = height
		self.image = pygame.image.load(image)

	def draw(self, screen, scrollPosX, scrollPosY):
		screen.blit(self.image, (self.x-scrollPosX, self.y-scrollPosY))

class Link(Sprite):
	def __init__(self, x, y, width, height, image):
		super().__init__(x, y, width, height, image) #initialize with parent class 
		#special variables needed for link:
		self.prev_x = 51
		self.prev_y = 51 
		self.speed = 10
		self.currentImage = 1 
		self.direction = 0
		self.NUM_IMAGES = 40
		self.MAX_IMAGES_PER_DIRECTION = 10
		self.images = [] #list of images for Link 

		#load all Link images into images list 
		for i in range(self.NUM_IMAGES):
			newImage = pygame.image.load("img/link" + str(i+1) + ".png")
			self.images.append(newImage)

	#link draw method 
	#@Override 
	def draw(self, screen, scrollPosX, scrollPosY):
		imageIndex = self.currentImage + self.direction * self.MAX_IMAGES_PER_DIRECTION
		newImageSize = (self.width,self.height)
		scaledImage = pygame.transform.scale(self.images[imageIndex], newImageSize)
		screen.blit(scaledImage, (self.x-scrollPosX, self.y-scrollPosY))

	#set previous coordinates for collision detection 
	def setPrevCoordinate(self):
		self.prev_x = self.x
		self.prev_y = self.y 

	#update image number in the link image list 
	def updateImageNum(self, direction):
		self.direction = direction
		self.currentImage += 1
		if self.currentImage >= self.MAX_IMAGES_PER_DIRECTION:
			self.currentImage = 1

	def avoidObstacle(self, obstruction, obstruction_x, obstruction_y):
		#fix collision with left of another sprite 
		if self.x + self.width >= obstruction_x and self.prev_x + self.width <= obstruction_x:
			self.x = obstruction_x - self.width 
		#fix collision with right of another sprite 
		if self.x <= obstruction_x + obstruction.width and self.prev_x >= obstruction_x + obstruction.width:
			self.x = obstruction_x + obstruction.width 
		#fix collision with top of another sprite 
		if self.y + self.height >= obstruction_y and self.prev_y + self.height <= obstruction_y:
			self.y = obstruction_y - self.height
		#fix collision with bottom of another sprite
		if self.y <= obstruction_y + obstruction.height and self.prev_y >= obstruction_y + obstruction.height:
			self.y = obstruction_y + obstruction.height 

	def update(self):
		pass 

class Tile(Sprite):
	def __init__(self, x, y, width, height, image):
		super().__init__(x, y, width, height, image) #initialize with parent class 
		#special variables needed for tile: none

	#tile draw method 
	#@Override 
	def draw(self, screen, scrollPosX, scrollPosY):
		screen.blit(self.image, (self.x-scrollPosX, self.y-scrollPosY))

	def update(self):
		pass 

class Boomerang(Sprite):
	def __init__(self, x, y, width, height, image):
		super().__init__(x, y, width, height, image) #initialize with parent class 
		#special variables needed for boomerang:
		self.speed = 11
		self.x_direction = None 
		self.y_direction = None 
		self.x_steps = 0 
		self.y_steps = 0 
		self.imageIndex = 0
		self.isActive = True
		self.images = []
		self.timer = 0
		self.moving = False 

		for i in range(4):
			newImage = pygame.image.load("img/boomerang" + str(i+1) + ".png")
			self.images.append(newImage)

	#boomerang draw method 
	#@Override 
	def draw(self, screen, scrollPosX, scrollPosY):
		newImageSize = (15,15)
		scaledImage = pygame.transform.scale(self.images[self.imageIndex], newImageSize)
		screen.blit(scaledImage, (self.x-scrollPosX, self.y-scrollPosY))

	#sets boomerang collided to True if collision occurs
	def collided(self):
		self.isActive = False; 

	#boomerang update method 
	def update(self):
		self.x += self.x_direction*self.speed
		if self.x_direction != 0:
			self.x_steps += 1
		if self.x_steps == 12:
			self.speed = self.speed/2 
		if self.x_steps == 20:
			self.x_direction = self.x_direction * (-1)
			self.speed = 13 

		self.y += self.y_direction*self.speed
		if self.y_direction != 0:
			self.y_steps += 1
		if self.y_steps == 12:
			self.speed = self.speed/2 
		if self.y_steps == 20:
			self.y_direction = self.y_direction * (-1)
			self.speed = 13

		self.imageIndex += 1
		if self.imageIndex == 4:
			self.imageIndex = 0

		self.timer += 3
		if self.timer == 30:
			self.moving = True 

		return self.isActive 
		
class Pot(Sprite):
	def __init__(self, x, y, width, height, image):
		super().__init__(x, y, width, height, image) #initialize with parent class 
		#special variables needed for pot:
		self.timer = 20
		self.shattered = False
		self.isActive = True
		self.x_direction = 0
		self.y_direction = 0
		self.speed = 10

	#pot draw method
	#@Override  
	def draw(self, screen, scrollPosX, scrollPosY):
		#resize the image before drawing 
		newImageSize = (35,35)
		scaledImage = pygame.transform.scale(self.image, newImageSize)
		if self.shattered == False:
			scaledImage = pygame.transform.scale(self.image, newImageSize)
			screen.blit(scaledImage, (self.x-scrollPosX, self.y-scrollPosY))
		elif self.shattered == True:
			self.image = pygame.image.load("img/pot_broken.png")
			scaledImage = pygame.transform.scale(self.image, newImageSize)
			screen.blit(scaledImage, (self.x-scrollPosX, self.y-scrollPosY))

	def update(self):
		if self.shattered == False:
			self.x += self.x_direction*self.speed
			self.y += self.y_direction*self.speed 
		if self.shattered == True:
			self.timer-=1 
		if self.shattered == True and self.timer == 0:
			return False 
		return True 
	
	def slide(self, direction):
		#slide down 
		if direction == 0:
			self.y_direction = 1
			self.x_direction = 0
		#slide left 
		if direction == 1:
			self.y_direction = 0
			self.x_direction = -1
		#slide right 
		if direction == 2:
			self.y_direction = 0
			self.x_direction = 1
		#slide up
		if direction == 3:
			self.y_direction = -1
			self.x_direction = 0

	def shatterPot(self):
		self.shattered = True 
		
class Model():
	# initialize model with list of sprites 
	def __init__(self):
		self.sprites = [] #declare a list of sprites
		#add each sprite manually into the list 
		self.link = Link(52,52,50,50,"img/link1.png")
		self.sprites.append(self.link)
		self.sprites.append(Tile(0,0,50,50,"img/tile.png"))
		self.sprites.append(Tile(1200,150,50,50,"img/tile.png"))
		self.sprites.append(Tile(1150,250,50,50,"img/tile.png"))
		self.sprites.append(Tile(950,250,50,50,"img/tile.png"))
		self.sprites.append(Tile(1050,850,50,50,"img/tile.png"))
		self.sprites.append(Tile(950,700,50,50,"img/tile.png"))
		self.sprites.append(Tile(100,750,50,50,"img/tile.png"))
		self.sprites.append(Tile(100,700,50,50,"img/tile.png"))
		self.sprites.append(Tile(0,0,50,50,"img/tile.png"))
		self.sprites.append(Tile(100,0,50,50,"img/tile.png"))
		self.sprites.append(Tile(50,0,50,50,"img/tile.png"))
		self.sprites.append(Tile(150,0,50,50,"img/tile.png"))
		self.sprites.append(Tile(0,50,50,50,"img/tile.png"))
		self.sprites.append(Tile(0,100,50,50,"img/tile.png"))
		self.sprites.append(Tile(0,150,50,50,"img/tile.png"))
		self.sprites.append(Tile(0,200,50,50,"img/tile.png"))
		self.sprites.append(Tile(0,250,50,50,"img/tile.png"))
		self.sprites.append(Tile(0,300,50,50,"img/tile.png"))
		self.sprites.append(Tile(0,350,50,50,"img/tile.png"))
		self.sprites.append(Tile(0,400,50,50,"img/tile.png"))
		self.sprites.append(Tile(200,0,50,50,"img/tile.png"))
		self.sprites.append(Tile(250,0,50,50,"img/tile.png"))
		self.sprites.append(Tile(300,0,50,50,"img/tile.png"))
		self.sprites.append(Tile(350,0,50,50,"img/tile.png"))
		self.sprites.append(Tile(400,0,50,50,"img/tile.png"))
		self.sprites.append(Tile(450,0,50,50,"img/tile.png"))
		self.sprites.append(Tile(500,0,50,50,"img/tile.png"))
		self.sprites.append(Tile(550,0,50,50,"img/tile.png"))
		self.sprites.append(Tile(600,0,50,50,"img/tile.png"))
		self.sprites.append(Tile(650,0,50,50,"img/tile.png"))
		self.sprites.append(Tile(650,50,50,50,"img/tile.png"))
		self.sprites.append(Tile(650,100,50,50,"img/tile.png"))
		self.sprites.append(Tile(650,150,50,50,"img/tile.png"))
		self.sprites.append(Tile(650,200,50,50,"img/tile.png"))
		self.sprites.append(Tile(650,250,50,50,"img/tile.png"))
		self.sprites.append(Tile(650,400,50,50,"img/tile.png"))
		self.sprites.append(Tile(650,450,50,50,"img/tile.png"))
		self.sprites.append(Tile(600,450,50,50,"img/tile.png"))
		self.sprites.append(Tile(550,450,50,50,"img/tile.png"))
		self.sprites.append(Tile(500,450,50,50,"img/tile.png"))
		self.sprites.append(Tile(450,450,50,50,"img/tile.png"))
		self.sprites.append(Tile(400,450,50,50,"img/tile.png"))
		self.sprites.append(Tile(350,450,50,50,"img/tile.png"))
		self.sprites.append(Tile(300,450,50,50,"img/tile.png"))
		self.sprites.append(Tile(250,450,50,50,"img/tile.png"))
		self.sprites.append(Tile(200,450,50,50,"img/tile.png"))
		self.sprites.append(Tile(0,450,50,50,"img/tile.png"))
		self.sprites.append(Tile(150,450,50,50,"img/tile.png"))
		self.sprites.append(Tile(200,400,50,50,"img/tile.png"))
		self.sprites.append(Tile(250,400,50,50,"img/tile.png"))
		self.sprites.append(Tile(300,400,50,50,"img/tile.png"))
		self.sprites.append(Tile(350,400,50,50,"img/tile.png"))
		self.sprites.append(Tile(400,400,50,50,"img/tile.png"))
		self.sprites.append(Tile(450,400,50,50,"img/tile.png"))
		self.sprites.append(Tile(500,400,50,50,"img/tile.png"))
		self.sprites.append(Tile(550,400,50,50,"img/tile.png"))
		self.sprites.append(Tile(600,400,50,50,"img/tile.png"))
		self.sprites.append(Tile(500,150,50,50,"img/tile.png"))
		self.sprites.append(Tile(450,150,50,50,"img/tile.png"))
		self.sprites.append(Tile(400,150,50,50,"img/tile.png"))
		self.sprites.append(Tile(450,200,50,50,"img/tile.png"))
		self.sprites.append(Tile(600,250,50,50,"img/tile.png"))
		self.sprites.append(Tile(350,200,50,50,"img/tile.png"))
		self.sprites.append(Tile(150,100,50,50,"img/tile.png"))
		self.sprites.append(Tile(700,250,50,50,"img/tile.png"))
		self.sprites.append(Tile(700,400,50,50,"img/tile.png"))
		self.sprites.append(Tile(700,200,50,50,"img/tile.png"))
		self.sprites.append(Tile(700,150,50,50,"img/tile.png"))
		self.sprites.append(Tile(700,100,50,50,"img/tile.png"))
		self.sprites.append(Tile(700,50,50,50,"img/tile.png"))
		self.sprites.append(Tile(700,0,50,50,"img/tile.png"))
		self.sprites.append(Tile(750,0,50,50,"img/tile.png"))
		self.sprites.append(Tile(800,0,50,50,"img/tile.png"))
		self.sprites.append(Tile(850,0,50,50,"img/tile.png"))
		self.sprites.append(Tile(900,0,50,50,"img/tile.png"))
		self.sprites.append(Tile(950,0,50,50,"img/tile.png"))
		self.sprites.append(Tile(1000,0,50,50,"img/tile.png"))
		self.sprites.append(Tile(1050,0,50,50,"img/tile.png"))
		self.sprites.append(Tile(1100,0,50,50,"img/tile.png"))
		self.sprites.append(Tile(1150,0,50,50,"img/tile.png"))
		self.sprites.append(Tile(1200,0,50,50,"img/tile.png"))
		self.sprites.append(Tile(1250,0,50,50,"img/tile.png"))
		self.sprites.append(Tile(1300,0,50,50,"img/tile.png"))
		self.sprites.append(Tile(1350,0,50,50,"img/tile.png"))
		self.sprites.append(Tile(1350,50,50,50,"img/tile.png"))
		self.sprites.append(Tile(1350,100,50,50,"img/tile.png"))
		self.sprites.append(Tile(1350,150,50,50,"img/tile.png"))
		self.sprites.append(Tile(1350,200,50,50,"img/tile.png"))
		self.sprites.append(Tile(1350,250,50,50,"img/tile.png"))
		self.sprites.append(Tile(1350,300,50,50,"img/tile.png"))
		self.sprites.append(Tile(1350,350,50,50,"img/tile.png"))
		self.sprites.append(Tile(1350,400,50,50,"img/tile.png"))
		self.sprites.append(Tile(1350,450,50,50,"img/tile.png"))
		self.sprites.append(Tile(1300,450,50,50,"img/tile.png"))
		self.sprites.append(Tile(1250,450,50,50,"img/tile.png"))
		self.sprites.append(Tile(1100,450,50,50,"img/tile.png"))
		self.sprites.append(Tile(1050,450,50,50,"img/tile.png"))
		self.sprites.append(Tile(1000,450,50,50,"img/tile.png"))
		self.sprites.append(Tile(950,450,50,50,"img/tile.png"))
		self.sprites.append(Tile(900,450,50,50,"img/tile.png"))
		self.sprites.append(Tile(850,450,50,50,"img/tile.png"))
		self.sprites.append(Tile(800,450,50,50,"img/tile.png"))
		self.sprites.append(Tile(750,450,50,50,"img/tile.png"))
		self.sprites.append(Tile(700,450,50,50,"img/tile.png"))
		self.sprites.append(Tile(950,300,50,50,"img/tile.png"))
		self.sprites.append(Tile(950,350,50,50,"img/tile.png"))
		self.sprites.append(Tile(950,400,50,50,"img/tile.png"))
		self.sprites.append(Tile(900,400,50,50,"img/tile.png"))
		self.sprites.append(Tile(900,350,50,50,"img/tile.png"))
		self.sprites.append(Tile(900,300,50,50,"img/tile.png"))
		self.sprites.append(Tile(850,350,50,50,"img/tile.png"))
		self.sprites.append(Tile(850,400,50,50,"img/tile.png"))
		self.sprites.append(Tile(800,400,50,50,"img/tile.png"))
		self.sprites.append(Tile(750,400,50,50,"img/tile.png"))
		self.sprites.append(Tile(750,200,50,50,"img/tile.png"))
		self.sprites.append(Tile(800,150,50,50,"img/tile.png"))
		self.sprites.append(Tile(850,100,50,50,"img/tile.png"))
		self.sprites.append(Tile(900,50,50,50,"img/tile.png"))
		self.sprites.append(Tile(850,50,50,50,"img/tile.png"))
		self.sprites.append(Tile(800,100,50,50,"img/tile.png"))
		self.sprites.append(Tile(800,50,50,50,"img/tile.png"))
		self.sprites.append(Tile(750,50,50,50,"img/tile.png"))
		self.sprites.append(Tile(750,100,50,50,"img/tile.png"))
		self.sprites.append(Tile(750,150,50,50,"img/tile.png"))
		self.sprites.append(Tile(1000,300,50,50,"img/tile.png"))
		self.sprites.append(Tile(1050,350,50,50,"img/tile.png"))
		self.sprites.append(Tile(1100,400,50,50,"img/tile.png"))
		self.sprites.append(Tile(1050,400,50,50,"img/tile.png"))
		self.sprites.append(Tile(1000,400,50,50,"img/tile.png"))
		self.sprites.append(Tile(1000,350,50,50,"img/tile.png"))
		self.sprites.append(Tile(1200,200,50,50,"img/tile.png"))
		self.sprites.append(Tile(1150,100,50,50,"img/tile.png"))
		self.sprites.append(Tile(1150,150,50,50,"img/tile.png"))
		self.sprites.append(Tile(1150,200,50,50,"img/tile.png"))
		self.sprites.append(Tile(1100,150,50,50,"img/tile.png"))
		self.sprites.append(Tile(1050,150,50,50,"img/tile.png"))
		self.sprites.append(Tile(1250,500,50,50,"img/tile.png"))
		self.sprites.append(Tile(1100,500,50,50,"img/tile.png"))
		self.sprites.append(Tile(1300,500,50,50,"img/tile.png"))
		self.sprites.append(Tile(1350,500,50,50,"img/tile.png"))
		self.sprites.append(Tile(1350,550,50,50,"img/tile.png"))
		self.sprites.append(Tile(1350,600,50,50,"img/tile.png"))
		self.sprites.append(Tile(1350,650,50,50,"img/tile.png"))
		self.sprites.append(Tile(1350,700,50,50,"img/tile.png"))
		self.sprites.append(Tile(1350,800,50,50,"img/tile.png"))
		self.sprites.append(Tile(1350,850,50,50,"img/tile.png"))
		self.sprites.append(Tile(1350,900,50,50,"img/tile.png"))
		self.sprites.append(Tile(1350,750,50,50,"img/tile.png"))
		self.sprites.append(Tile(1050,500,50,50,"img/tile.png"))
		self.sprites.append(Tile(1000,500,50,50,"img/tile.png"))
		self.sprites.append(Tile(950,500,50,50,"img/tile.png"))
		self.sprites.append(Tile(900,500,50,50,"img/tile.png"))
		self.sprites.append(Tile(850,500,50,50,"img/tile.png"))
		self.sprites.append(Tile(800,500,50,50,"img/tile.png"))
		self.sprites.append(Tile(750,500,50,50,"img/tile.png"))
		self.sprites.append(Tile(700,500,50,50,"img/tile.png"))
		self.sprites.append(Tile(700,550,50,50,"img/tile.png"))
		self.sprites.append(Tile(700,600,50,50,"img/tile.png"))
		self.sprites.append(Tile(700,650,50,50,"img/tile.png"))
		self.sprites.append(Tile(700,800,50,50,"img/tile.png"))
		self.sprites.append(Tile(700,850,50,50,"img/tile.png"))
		self.sprites.append(Tile(750,900,50,50,"img/tile.png"))
		self.sprites.append(Tile(800,900,50,50,"img/tile.png"))
		self.sprites.append(Tile(850,900,50,50,"img/tile.png"))
		self.sprites.append(Tile(900,900,50,50,"img/tile.png"))
		self.sprites.append(Tile(950,900,50,50,"img/tile.png"))
		self.sprites.append(Tile(1000,900,50,50,"img/tile.png"))
		self.sprites.append(Tile(1050,900,50,50,"img/tile.png"))
		self.sprites.append(Tile(1100,900,50,50,"img/tile.png"))
		self.sprites.append(Tile(1150,900,50,50,"img/tile.png"))
		self.sprites.append(Tile(1200,900,50,50,"img/tile.png"))
		self.sprites.append(Tile(1250,900,50,50,"img/tile.png"))
		self.sprites.append(Tile(1300,900,50,50,"img/tile.png"))
		self.sprites.append(Tile(700,900,50,50,"img/tile.png"))
		self.sprites.append(Tile(700,950,50,50,"img/tile.png"))
		self.sprites.append(Tile(750,950,50,50,"img/tile.png"))
		self.sprites.append(Tile(800,950,50,50,"img/tile.png"))
		self.sprites.append(Tile(850,950,50,50,"img/tile.png"))
		self.sprites.append(Tile(900,950,50,50,"img/tile.png"))
		self.sprites.append(Tile(950,950,50,50,"img/tile.png"))
		self.sprites.append(Tile(1000,950,50,50,"img/tile.png"))
		self.sprites.append(Tile(1050,950,50,50,"img/tile.png"))
		self.sprites.append(Tile(1100,950,50,50,"img/tile.png"))
		self.sprites.append(Tile(1150,950,50,50,"img/tile.png"))
		self.sprites.append(Tile(1200,950,50,50,"img/tile.png"))
		self.sprites.append(Tile(1250,950,50,50,"img/tile.png"))
		self.sprites.append(Tile(1300,950,50,50,"img/tile.png"))
		self.sprites.append(Tile(1350,950,50,50,"img/tile.png"))
		self.sprites.append(Tile(1100,850,50,50,"img/tile.png"))
		self.sprites.append(Tile(1150,850,50,50,"img/tile.png"))
		self.sprites.append(Tile(1200,850,50,50,"img/tile.png"))
		self.sprites.append(Tile(1250,850,50,50,"img/tile.png"))
		self.sprites.append(Tile(1200,800,50,50,"img/tile.png"))
		self.sprites.append(Tile(1200,750,50,50,"img/tile.png"))
		self.sprites.append(Tile(1200,700,50,50,"img/tile.png"))
		self.sprites.append(Tile(950,750,50,50,"img/tile.png"))
		self.sprites.append(Tile(1000,800,50,50,"img/tile.png"))
		self.sprites.append(Tile(900,750,50,50,"img/tile.png"))
		self.sprites.append(Tile(850,750,50,50,"img/tile.png"))
		self.sprites.append(Tile(900,650,50,50,"img/tile.png"))
		self.sprites.append(Tile(1000,700,50,50,"img/tile.png"))
		self.sprites.append(Tile(1050,650,50,50,"img/tile.png"))
		self.sprites.append(Tile(1050,700,50,50,"img/tile.png"))
		self.sprites.append(Tile(1150,800,50,50,"img/tile.png"))
		self.sprites.append(Tile(650,650,50,50,"img/tile.png"))
		self.sprites.append(Tile(650,800,50,50,"img/tile.png"))
		self.sprites.append(Tile(0,500,50,50,"img/tile.png"))
		self.sprites.append(Tile(150,500,50,50,"img/tile.png"))
		self.sprites.append(Tile(0,550,50,50,"img/tile.png"))
		self.sprites.append(Tile(0,600,50,50,"img/tile.png"))
		self.sprites.append(Tile(0,650,50,50,"img/tile.png"))
		self.sprites.append(Tile(0,700,50,50,"img/tile.png"))
		self.sprites.append(Tile(0,750,50,50,"img/tile.png"))
		self.sprites.append(Tile(0,800,50,50,"img/tile.png"))
		self.sprites.append(Tile(0,850,50,50,"img/tile.png"))
		self.sprites.append(Tile(0,900,50,50,"img/tile.png"))
		self.sprites.append(Tile(50,900,50,50,"img/tile.png"))
		self.sprites.append(Tile(200,500,50,50,"img/tile.png"))
		self.sprites.append(Tile(250,500,50,50,"img/tile.png"))
		self.sprites.append(Tile(300,500,50,50,"img/tile.png"))
		self.sprites.append(Tile(350,500,50,50,"img/tile.png"))
		self.sprites.append(Tile(400,500,50,50,"img/tile.png"))
		self.sprites.append(Tile(450,500,50,50,"img/tile.png"))
		self.sprites.append(Tile(550,500,50,50,"img/tile.png"))
		self.sprites.append(Tile(500,500,50,50,"img/tile.png"))
		self.sprites.append(Tile(600,500,50,50,"img/tile.png"))
		self.sprites.append(Tile(650,500,50,50,"img/tile.png"))
		self.sprites.append(Tile(650,550,50,50,"img/tile.png"))
		self.sprites.append(Tile(650,600,50,50,"img/tile.png"))
		self.sprites.append(Tile(650,850,50,50,"img/tile.png"))
		self.sprites.append(Tile(650,900,50,50,"img/tile.png"))
		self.sprites.append(Tile(100,900,50,50,"img/tile.png"))
		self.sprites.append(Tile(150,900,50,50,"img/tile.png"))
		self.sprites.append(Tile(200,900,50,50,"img/tile.png"))
		self.sprites.append(Tile(250,900,50,50,"img/tile.png"))
		self.sprites.append(Tile(300,900,50,50,"img/tile.png"))
		self.sprites.append(Tile(350,900,50,50,"img/tile.png"))
		self.sprites.append(Tile(400,900,50,50,"img/tile.png"))
		self.sprites.append(Tile(450,900,50,50,"img/tile.png"))
		self.sprites.append(Tile(500,900,50,50,"img/tile.png"))
		self.sprites.append(Tile(550,900,50,50,"img/tile.png"))
		self.sprites.append(Tile(600,900,50,50,"img/tile.png"))
		self.sprites.append(Tile(0,950,50,50,"img/tile.png"))
		self.sprites.append(Tile(50,950,50,50,"img/tile.png"))
		self.sprites.append(Tile(100,950,50,50,"img/tile.png"))
		self.sprites.append(Tile(150,950,50,50,"img/tile.png"))
		self.sprites.append(Tile(200,950,50,50,"img/tile.png"))
		self.sprites.append(Tile(250,950,50,50,"img/tile.png"))
		self.sprites.append(Tile(300,950,50,50,"img/tile.png"))
		self.sprites.append(Tile(350,950,50,50,"img/tile.png"))
		self.sprites.append(Tile(400,950,50,50,"img/tile.png"))
		self.sprites.append(Tile(450,950,50,50,"img/tile.png"))
		self.sprites.append(Tile(500,950,50,50,"img/tile.png"))
		self.sprites.append(Tile(550,950,50,50,"img/tile.png"))
		self.sprites.append(Tile(600,950,50,50,"img/tile.png"))
		self.sprites.append(Tile(650,950,50,50,"img/tile.png"))
		self.sprites.append(Tile(50,700,50,50,"img/tile.png"))
		self.sprites.append(Tile(150,750,50,50,"img/tile.png"))
		self.sprites.append(Tile(200,750,50,50,"img/tile.png"))
		self.sprites.append(Tile(250,800,50,50,"img/tile.png"))
		self.sprites.append(Tile(250,700,50,50,"img/tile.png"))
		self.sprites.append(Tile(250,750,50,50,"img/tile.png"))
		self.sprites.append(Tile(250,650,50,50,"img/tile.png"))
		self.sprites.append(Tile(300,750,50,50,"img/tile.png"))
		self.sprites.append(Tile(350,700,50,50,"img/tile.png"))
		self.sprites.append(Tile(500,550,50,50,"img/tile.png"))
		self.sprites.append(Tile(450,600,50,50,"img/tile.png"))
		self.sprites.append(Tile(500,600,50,50,"img/tile.png"))
		self.sprites.append(Tile(550,600,50,50,"img/tile.png"))
		self.sprites.append(Tile(500,650,50,50,"img/tile.png"))
		self.sprites.append(Tile(500,700,50,50,"img/tile.png"))
		self.sprites.append(Tile(500,750,50,50,"img/tile.png"))
		self.sprites.append(Tile(100,650,50,50,"img/tile.png"))
		self.sprites.append(Tile(50,650,50,50,"img/tile.png"))
		self.sprites.append(Tile(50,600,50,50,"img/tile.png"))
		self.sprites.append(Tile(350,250,50,50,"img/tile.png"))
		self.sprites.append(Tile(150,50,50,50,"img/tile.png"))
		self.sprites.append(Tile(150,150,50,50,"img/tile.png"))
		self.sprites.append(Tile(150,200,50,50,"img/tile.png"))
		self.sprites.append(Tile(150,250,50,50,"img/tile.png"))
		self.sprites.append(Tile(400,250,50,50,"img/tile.png"))
		self.sprites.append(Tile(400,200,50,50,"img/tile.png"))
		self.sprites.append(Tile(500,100,50,50,"img/tile.png"))
		self.sprites.append(Tile(450,50,50,50,"img/tile.png"))
		self.sprites.append(Tile(450,100,50,50,"img/tile.png"))
		self.sprites.append(Tile(1150,50,50,50,"img/tile.png"))
		self.sprites.append(Tile(300,250,50,50,"img/tile.png"))
		self.sprites.append(Tile(450,550,50,50,"img/tile.png"))
		self.sprites.append(Tile(500,50,50,50,"img/tile.png"))
		self.sprites.append(Pot(1008,115,35,35,"img/pot_normal.png"))
		self.sprites.append(Pot(1283,118,35,35,"img/pot_normal.png"))
		self.sprites.append(Pot(1283,243,35,35,"img/pot_normal.png"))
		self.sprites.append(Pot(1298,387,35,35,"img/pot_normal.png"))
		self.sprites.append(Pot(1318,598,35,35,"img/pot_normal.png"))
		self.sprites.append(Pot(1274,755,35,35,"img/pot_normal.png"))
		self.sprites.append(Pot(1155,747,35,35,"img/pot_normal.png"))
		self.sprites.append(Pot(1140,593,35,35,"img/pot_normal.png"))
		self.sprites.append(Pot(1057,590,35,35,"img/pot_normal.png"))
		self.sprites.append(Pot(1004,646,35,35,"img/pot_normal.png"))
		self.sprites.append(Pot(938,855,35,35,"img/pot_normal.png"))
		self.sprites.append(Pot(862,607,35,35,"img/pot_normal.png"))
		self.sprites.append(Pot(822,688,35,35,"img/pot_normal.png"))
		self.sprites.append(Pot(386,859,35,35,"img/pot_normal.png"))
		self.sprites.append(Pot(598,840,35,35,"img/pot_normal.png"))
		self.sprites.append(Pot(98,858,35,35,"img/pot_normal.png"))
		self.sprites.append(Pot(189,673,35,35,"img/pot_normal.png"))
		self.sprites.append(Pot(165,715,35,35,"img/pot_normal.png"))
		self.sprites.append(Pot(314,709,35,35,"img/pot_normal.png"))
		self.sprites.append(Pot(241,174,35,35,"img/pot_normal.png"))
		self.sprites.append(Pot(304,73,35,35,"img/pot_normal.png"))
		self.sprites.append(Pot(404,105,35,35,"img/pot_normal.png"))
		self.sprites.append(Pot(589,107,35,35,"img/pot_normal.png"))

	def update(self):
		#nested for loops that iterate through sprites list 
		for sprite1 in self.sprites:
			for sprite2 in self.sprites:
				if self.isColliding(sprite1, sprite2):
					#if link collides with a tile, avoid it 
					if (isinstance(sprite1, Link) and isinstance(sprite2, Tile)):
						self.link.avoidObstacle(sprite2, sprite2.x, sprite2.y)
					
					#remove boomerang if it collides with a tile 
					if isinstance(sprite1, Boomerang) and isinstance(sprite2, Tile):
						sprite1.collided()

					#remove boomerang if it collides with a pot 
					if isinstance(sprite1, Boomerang) and isinstance(sprite2, Pot):
						sprite1.collided()

					#shatter pot if boomerang collides with the pot
					if isinstance(sprite1, Boomerang) and isinstance(sprite2, Pot):
						sprite2.shatterPot()

					#if link walks into a pot, make the pot slide 
					if isinstance(sprite1, Link) and isinstance(sprite2, Pot):
						sprite2.slide(self.link.direction)

					#if pot collides with a tile, shatter and remove the pot
					if isinstance(sprite1, Tile) and isinstance(sprite2, Pot):
						sprite2.shatterPot()

					#link can catch boomerang if it returns to him 
					if (isinstance(sprite1, Boomerang) and sprite1.moving == True) and isinstance(sprite2, Link):
						sprite1.collided()

					#remove sliding pots if they collide with each other 
					if isinstance(sprite1, Pot) and isinstance(sprite2, Pot) and sprite1!=sprite2:
						sprite1.shatterPot()
						sprite2.shatterPot() 

			if sprite1.update() == False:
				self.sprites.remove(sprite1)

	def isColliding(self, sprite_a, sprite_b):
		a_left = sprite_a.x
		a_right = sprite_a.x + sprite_a.width
		a_top = sprite_a.y
		a_bottom = sprite_a.y + sprite_a.height 
		
		if a_right < sprite_b.x:
			return False
		if a_left > sprite_b.x + sprite_b.height:
			return False 
		if a_bottom < sprite_b.y:
			return False
		if a_top > sprite_b.y + sprite_b.height:
			return False 
		#return true if none of the collision conditions are met 
		return True 
	
	def throwBoomerang(self, x_throwDirection, y_throwDirection):
		boomerangX = self.link.x+(self.link.width/2)
		boomerangY = self.link.y+(self.link.height/2)
		b = Boomerang(boomerangX, boomerangY, 30, 30, "img/boomerang1.png") #create a new boomerang 
		b.x_direction = x_throwDirection
		b.y_direction = y_throwDirection
		self.sprites.append(b)

class View():
	def __init__(self, model):
		screen_size = (700,500) #set view size 
		self.screen = pygame.display.set_mode(screen_size, 32)
		self.model = model
		self.scrollPosX = 0
		self.scrollPosY = 0

	# update view 
	def update(self):
		self.screen.fill([26,64,159])

		#draw sprite images within sprite list using for loops 
		for sprite in self.model.sprites:
			sprite.draw(self.screen, self.scrollPosX, self.scrollPosY) 
		pygame.display.flip()

		#adjust view area if link moves out of current viewing area 
		if self.model.link.x >= 700:
			self.scrollPosX = 700
		if self.model.link.y >= 500:
			self.scrollPosY = 500
		if self.model.link.x <700:
			self.scrollPosX = 0
		if self.model.link.y <500:
			self.scrollPosY = 0

class Controller():
	# initialize controller 
	def __init__(self, model):
		self.model = model
		self.keep_going = True
		self.keyCtrl = False 

	def update(self):
		for event in pygame.event.get():
			if event.type == QUIT:
				self.keep_going = False
			elif event.type == KEYDOWN:
				if event.key == K_ESCAPE or event.key == K_q:
					self.keep_going = False
			elif event.type == KEYUP: 
				if event.key == K_LCTRL:
					self.keyCtrl = True 
				if event.key == K_RCTRL:
					self.keyCtrl = True 
		keyDown = pygame.key.get_pressed()

		#set previous coordinates for Link for collision fixing 
		self.model.link.setPrevCoordinate() 
		
		if keyDown[K_LEFT]:
			self.model.link.updateImageNum(1)
			self.model.link.x -= self.model.link.speed 

		if keyDown[K_RIGHT]:
			self.model.link.updateImageNum(2)
			self.model.link.x += self.model.link.speed 

		if keyDown[K_UP]:
			self.model.link.updateImageNum(3)
			self.model.link.y -= self.model.link.speed 

		if keyDown[K_DOWN]:
			self.model.link.updateImageNum(0)
			self.model.link.y += self.model.link.speed 

		if self.keyCtrl == True:
            #0 = down, 1 = left, 2 = right, 3 = up
			#throw right 
			if self.model.link.direction == 2:
				self.model.throwBoomerang(1,0)
			#throw left 
			if self.model.link.direction == 1:
				self.model.throwBoomerang(-1,0)
			#throw up
			if self.model.link.direction ==3:
				self.model.throwBoomerang(0,-1)
			#throw down 
			if self.model.link.direction == 0:
				self.model.throwBoomerang(0,1)
			self.keyCtrl = False 

print("Use the arrow keys to move. Press Esc or Q to quit.")
pygame.init()
m = Model()
v = View(m)
c = Controller(m)
while c.keep_going:
	c.update()
	m.update()
	v.update()
	sleep(0.025)
print("Game exited, thanks for playing!")
