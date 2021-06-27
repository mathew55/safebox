# SafeBox: A privacy first Streaming Server
<p> SafeBox is an attempt to create a privacy first client-server system built on top of akka- streams. </p>
<p> SafeBox comes with the following features: </p>
<p>1. A client, which can select a file and upload it to server. A client can download the previously uploaded file from the server </p>
<p>2. A server, which receives files from clients, and serves the file back to them upon request. </p>
<p>3. Uses streams to stream files from client to server and back </p>
<p>4. Clients encrypt a file before uploading it to a server. Once downloaded, the client also decrypts the file. BouncyCastle library is used for this purpose</p>
