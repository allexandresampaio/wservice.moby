#include <ESP8266WiFi.h>

//Dados WIFI  
const char* ssid = "dd-wrt";
const char* password = "ddwrt#$16";

IPAddress server(192,168,1,117);
int servoPin = 2;
char c;
WiFiClient client;
 
void setup() {
  pinMode(servoPin, OUTPUT);
  pinMode(1, OUTPUT);
  digitalWrite(servoPin, LOW);
  digitalWrite(1, LOW);
  Serial.begin(115200);
  delay(10);

 //Definição GPIO
  
 
  // Connect to WiFi network
  Serial.println();
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);
  
  WiFi.begin(ssid, password);
  
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
    digitalWrite(1, LOW);
  }
  
  Serial.println("");
  Serial.println("Conectado ao WIFI");
  digitalWrite(1, HIGH);
  

}

void getDirecao(){
  if (client.connect(server, 8080)) {
    Serial.println("connected");
    client.println("GET /wservice.moby/v1/ws/direcoes");
    client.println("Host: http://192.168.1.118");
    client.println("Connection: close");
    client.println();
  } else {
    Serial.println("connection failed");
  }

  delay(4000);
  
  }
 
void loop() {
   getDirecao();
   
  if(client.available()) {
     c = client.read();
    Serial.print(c);
  }

  switch (c) {

    case '1':
      Serial.println("caso for esquerda");
      digitalWrite(servoPin, HIGH);
      delay(500);
      digitalWrite(servoPin, LOW);
      delay(500);
      digitalWrite(servoPin, HIGH);
      delay(500);
      digitalWrite(servoPin, LOW);
      delay(500);
       break;
       
     case '2':
      Serial.println("caso for direita");
      digitalWrite(servoPin, HIGH);
      delay(1000);
      digitalWrite(servoPin, LOW);
      delay(1000);
      break;
     
     case '3':
      Serial.println("caso for perigo");
      digitalWrite(servoPin, HIGH);
      delay(4000);
      digitalWrite(servoPin, LOW);
      delay(4000);
     break;
     default:
       digitalWrite(servoPin, LOW);
    break;
  }
  
  if (!client.connected()) {
    Serial.println();
    Serial.println("disconnecting.");
    client.stop();
    }
    
}
