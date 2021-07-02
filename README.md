# sound-cloud

File exchange platforms like SoundCloud allow musicians to share their creations directly with their fans. For this purpose, they can upload music files accompanied by various meta-information (title, author, interpreter, genre, ...). Meta-information serves so that listeners are aware of shared files and can carry out searches. 

Having found the desired files, they can then download them for later use.
Bearing in mind that the files to be exchanged are of considerable size, usually with several MB, the implementation of these systems has to pay particular attention to the resources consumed with the storage, manipulation and transmission of these files. 

In particular, it is important to limit the number of simultaneous operations that can be carried out in order not to overload the system and to maintain a relative fairness between the different users.

In this project, it was purposed the development of a platform for sharing music files in the form of a client/server in Java using sockets and threads. 

## Basic features: 
* Authentication and register of a user, given the name and password. Whenever an user wishes to interact with the service, it must establish a conection and be authenticated by the server.
* Publish a music file, providing its content and meta-information (title, interpreter, year and a variable amount of tags), receiving an unique identifier.
* Perform a music search by sending a search tag and receiving back a list of the songs that match it. For each song, its meta-information must also be received:
   - unique identifier;
   - title, artist, year and a variable number of tags, provided when the song is loaded;
   - number of times the song was downloaded.
* Download a music file, providing its unique identifier. 

## Client
A client that offers a user interface that supports the functionality described above must be provided. 
This client should be written in Java using TCP threads and sockets.

## Server
The server should also be written in Java, using TCP threads and sockets, keeping in memory the relevant information to support the functionalities, receive connections and input from clients, as well as sending the desired information to them. 
The protocol between client and server should be text-based, line-oriented (except for the content of the files themselves). In order for the server not to be vulnerable to slow clients, each thread should not write to more than one socket. 

__Keywords__: Java, sockets, TCP, threads


## Collaborators

| Name            	|
|-----------------	|
| [Carolina Cunha](https://github.com/13caroline)  	|
| [Hugo Faria](https://github.com/KHiro13)      	|
| [João Diogo Mota](https://github.com/JoaoDiogoMota) 	|

> <img src="https://seeklogo.com/images/U/Universidade_do_Minho-logo-CB2F98451C-seeklogo.com.png" align="left" height="48" width="48" > University of Minho, Software Engineering (3rd Year).
