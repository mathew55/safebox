# SafeBox: A Privacy First Streaming Server
<p> SafeBox is a first step in building a privacy first cloud storage system built on top of akka-streams </p>

<p> SafeBox comes with the following features: </p>
<p>1. A client, which can select a file and upload it to server. A client can download the previously uploaded file from the server </p>
<p>2. A server, which receives files from clients, and serves the file back to them upon request. </p>
<p>3. Uses streams to stream files from client to server and back to have seamless upload/download of large files</p>
<p>4. Clients encrypt a file before uploading it to a server. Once downloaded, the client also decrypts the file. BouncyCastle library is used for this purpose</p>


## How to start safebox server
 <p>1. Clone the repo into a local directory</p>
 <p>2. Safebox-server directory contains all the code related to server functionality</p>
 <p>3. Ensure scala(v2.12.8) and sbt are available in your system, Docker support coming soon </p>
 <p>4. Execute sbt run to start the server, The server will start itself at port 5000</p>
 
## How to use safebox client for file uploads & downloads
 <p>1. Safebox-client directory contains all the code related to client functionality</p>
 <p>2. Ensure scala(v2.12.8) and maven are available in your system </p>
 <p>3. Build code by executing mvn clean install</p>
 <p>4. For file uploads: run command 'java -jar safebox-client.jar upload {filepath} {encryptionKey}'</p>
 <p>5. For file downloads: run command 'java -jar safebox-client.jar download {filepath} {decryptionKey}'</p>
 
## Server API Reference
<p> 1. Upload File: /upload, To upload a file to server, Note: Using this direclty will upload a file without encryption. </p>
<p> 2. List Files: /list, To list all uploaded files in server.
<p> 3. Download File: /download?fileName={Name of file}, To download a file from server </p>
<p> 4. Delete File: /delete?fileName={Name of file}, To delete a file from server </p>

 
 
