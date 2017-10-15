/* The harware uses was an esp8266 
 *  set up the esp and flash it with nodeMCU
 *  Chnage GPIO or GPI1 to the according pin per device
 *  Also chnage the output/input command for the esp 
 *  inlcude the wifi user, password and port number 
 *  these need to match with the app
 *  
 *  
 * yellow = GPI1
 * white  = GPI0  yellow
 * gray   = RX    blue
 * purple = TX    green
 * 
 * 
 *  
 */

#include <ESP8266WiFi.h>

char* ssid = "****";
char* password = "*****";
const int GPI0 = 0;
const int GPI1 = 2;

long unsigned device1 = 0;
long unsigned device2 = 0;

bool device1T = false;
bool device2T = false; 

long unsigned TIME = millis();


WiFiServer server(2222);


void printWiFiStatus(), timeCheck();


void setup(void) {
  Serial.begin(115200);
  
  pinMode(GPI0, OUTPUT);
  pinMode(GPI1, OUTPUT);
  
  Serial.begin(115200);
  WiFi.begin(ssid, password);

  // Configure GPIO2 as OUTPUT.

  
  // Start TCP server.
  server.begin();
}

void loop(void) {

  
  
  // Check if module is still connected to WiFi.
  if (WiFi.status() != WL_CONNECTED) {
    while (WiFi.status() != WL_CONNECTED) {
      //WiFi.begin(ssid, password);
      delay(500);
    }
    // Print the new IP to Serial.
    printWiFiStatus();
  }


  
  WiFiClient client = server.available();

//////////////////////////////////////////////////////
 timeCheck();
 /////////////////////////////////////////////////////// 
  if (client ) {
    Serial.println("Client connected.\n");
    
    while (client.connected()) {

     
      if (client.available()) {
       client.setTimeout(500);
       String command = client.readStringUntil('\n');
       client.flush();
       Serial.println(command);
       if (command.charAt(0) == 'H') {
        Serial.println("Relay is 1 now on.");
          if(command.charAt(1) == '1'){
            device1 = (command.charAt(2)-48)*1000;
            device1 += (command.charAt(3)-48)*100;
            device1 += (command.charAt(4)-48)*10;
            device1 += (command.charAt(5)-48)*1;
            device1T = true;
            Serial.printf("timer set for: %d min\n",device1);
            device1 = device1*60*1000+ millis();
          
        }
        digitalWrite(GPI0, LOW);
        client.write("1");
        client.flush();
        }
        else if (command.charAt(0) == ('L')) {
          digitalWrite(GPI0, HIGH);
          Serial.println("Relay is now off.");
          client.write("0");
          client.flush();
          
        }
        else if (command.charAt(0) == 'O') {
          digitalWrite(GPI1, LOW);
          
          Serial.println("Relay 2 is now on.");
         if(command.charAt(1) == '1'){
          device2 = (command.charAt(2)-48)*1000;
          device2 += (command.charAt(3)-48)*100;
          device2 += (command.charAt(4)-48)*10;
          device2 += (command.charAt(5)-48)*1;
          device2T= true;
          Serial.printf("timer set for: %d min\n",device2);
          device2 = device2*60*1000+ millis();
         
         }
  
          client.write("1");
          client.flush();
        }
        else if (command.charAt(0) == 'F') {
          digitalWrite(GPI1, HIGH);
          Serial.println("Relay 2 is now Off.");
          client.write("1");
          client.flush();
        }
      }
      
      timeCheck();
    }
   ///////////////////////////////////////////////////////
    Serial.println("Client disconnected.");
    client.stop();
  }
  ////////////////////////////////////////////////////////
  
}
void timeCheck(){
  if(TIME> millis()){
    if(device1T || device2T){
      digitalWrite(GPI1, HIGH);
      digitalWrite(GPI0, HIGH);
    }
  }
  
  TIME = millis();
  
  if(device1T && (device1<TIME)){
      digitalWrite(GPI0, HIGH);
      device1T = false;
      Serial.println("Relay 1 is now Off.");
  }

  if(device2T && (device2)< TIME){
    digitalWrite(GPI1, HIGH);
    device2T = false;
    Serial.println("Relay 2 is now Off.");
  }

  //Serial.printf("the time in min is %ld\n",TIME);
}
void printWiFiStatus() {
  Serial.println("");
  Serial.print("Connected to ");
  Serial.println(ssid);
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());
}



/*const int rp = 4;
const int p;
#include <ESP8266WiFi.h>

const char* ssid = "manju";
const char* password = "12345678";

// Create an instance of the server
// specify the port to listen on as an argument
WiFiServer server(80);

void setup() {
 Serial.begin(115200);
 delay(10);

 // prepare GPIO2
pinMode(rp, OUTPUT);
analogWrite(rp, 0);
Serial.println();
 Serial.println();
 Serial.print("Connecting to ");
 Serial.println(ssid);
 
 WiFi.begin(ssid, password);
 
 while (WiFi.status() != WL_CONNECTED) {
   delay(500);
   Serial.print(".");
 }
 Serial.println("");
 Serial.println("WiFi connected");
 
 // Start the server
 server.begin();
 Serial.println("Server started");

 // Print the IP address
 Serial.println(WiFi.localIP());
}
void loop() {
 // Check if a client has connected
 WiFiClient client = server.available();
 if (!client) {
   return;
 }
 
 // Wait until the client sends some data
 Serial.println("new client");
 while(!client.available()){
   delay(1);
 }
 
 // Read the first line of the request
 String req = client.readStringUntil('\r');
 Serial.println(req);
 client.flush();
while(client.available()>0)
{
char a=(client.read()-48)*100;
char b=(client.read()-48)*10;
char c=(client.read()-48);
}
p=(a+b+c);
setcolor(p);
   client.flush();
   String s = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n<!DOCTYPE HTML>\r\n<html>\r\nTHE PWM VALUE is  ";
   s += "</html>\n";
   client.print(P);
}
void setcolor(int r)
{
 analogWrite(rp,r);
}*/
