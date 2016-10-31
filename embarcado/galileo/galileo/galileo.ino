#include <ArduinoJson.h>
#include <Ethernet.h>
#include <SPI.h>
#include "RFID.h"
#include <Servo.h>

// SETANDO OS PINOS DOS SENSORES ULTRASONICOS
//const int PIN_TRIGGER_D   = 1;  // D  =  DIREITA
//const int PIN_ECHO_D      = 2;  // D  =  DIREITA
//const int PIN_TRIGGER_E   = 3;  // E  =  ESQUERDA
//const int PIN_ECHO_E      = 4;  // E  =  ESQUERDA
const int PIN_TRIGGER_FR  = 1;  // FR =  FRENTE Marron
const int PIN_ECHO_FR     = 2;  // FR =  FRENTE amarelo

const int PIN_TRIGGER_FU  = 3;  // FU =  FUNDO roxo 
const int PIN_ECHO_FU     = 4;  // FU =  FUNDO laranja

const int PIN_SERVO       = 5;

// SETANDO PINOS DO LEITOR RFID
const int PIN_SS  = 10;
const int PIN_RST = 9;

String direcao;

RFID rfid(PIN_SS, PIN_RST);

// CONFIGURAÇÕES DE REDE 
byte mac[] = {0x98, 0x4F, 0xEE, 0x05, 0x58, 0xA4};
byte ip[] = { 192, 168, 1, 114 };
EthernetClient cliente;

// DEFINIÇÃO DO OBJETO JSON 
StaticJsonBuffer<200> jsonBuffer;
JsonObject& dados = jsonBuffer.createObject();

// DEFINIÇÃO DO OBJETO SERVOMOTOR 
Servo myservo;

String uid = "";
          
void setup() {
  Serial.begin(9600);
  // INICIANDO PINOS DOS SENSORES ULTRASONICO
  // D
  //pinMode(PIN_TRIGGER_D, OUTPUT_FAST);
  //pinMode(PIN_ECHO_D, INPUT_FAST);
  // E
  //pinMode(PIN_TRIGGER_E, OUTPUT_FAST);
  //pinMode(PIN_ECHO_E, INPUT_FAST);
  //FR
  myservo.attach(PIN_SERVO);
  pinMode(PIN_TRIGGER_FR, OUTPUT_FAST);
  pinMode(PIN_ECHO_FR, INPUT_FAST);
  //FU
  pinMode(PIN_TRIGGER_FU, OUTPUT_FAST);
  pinMode(PIN_ECHO_FU, INPUT_FAST);

  //INICIALIZANDO LEITOR RFID
  SPI.begin();
  rfid.init();
  //INICIALIZANDO REDE 
  Ethernet.begin(mac, ip);

}

void loop() {
  long disFrente   = medirDistancia(PIN_TRIGGER_FR, PIN_ECHO_FR);
  long disFundo    = medirDistancia(PIN_TRIGGER_FU, PIN_ECHO_FU);

  if(disFrente  < 10){
      Serial.println(disFrente);
      direcao =  "norte";
      Serial.println(direcao);
      myservo.write(0);
      delay(1000);
      capturaImagem();
      uid = getUid();
      Serial.println(uid);
      if(uid.length() != 0){
        preparaJson(dados);
        enviaDados(); }
      
  } else if(disFundo < 10){
      Serial.println(disFundo);
      direcao =  "sul";
      Serial.println(direcao);
      myservo.write(170);
      delay(1000);
      capturaImagem();
      uid = getUid();
      Serial.println(uid);
      if(uid.length() != 0){
        preparaJson(dados);
        enviaDados(); 
        }
 }
    delay(1000);

}

void capturaImagem(){ 
   //system("python /home/root/opencv-captura.py"); 
  }

void enviaDados() {
  Serial.println("Connectando...");
if (cliente.connect("192.168.1.117",8080))
{                                   
Serial.println("Sending to Server: ");                    
cliente.println("POST /wservice.moby/v1/ws/direcionamentoMapsNovo/ HTTP/1.1");           
Serial.print("POST / HTTP/1.1");           
cliente.println("Host: 192.168.1.117");
cliente.println("Content-Type:  application/json");
cliente.println("Connection: close");
cliente.println("User-Agent: Arduino/1.0");
cliente.print("Content-Length: ");
cliente.println(dados.measureLength());
cliente.println();
dados.printTo(cliente);
cliente.println();
dados.printTo(Serial);                                         
}else {
    Serial.println("Nao foi possivel conectar com o servidor");
  }
    
  if(cliente.available()) {
    char c = cliente.read();
    Serial.print(c);
  }
  if(cliente.connected()) {
    Serial.println();
    Serial.println("desconectando");
    cliente.stop();
  }
}
/* ==== METODOS DO JSON ==== */
void preparaJson(JsonObject& object) {
  object["id"]              =  uid; 
  object["localAtual"]      =  "-14.843094,-40.876080";
  object["posicaoRelativa"] =  direcao;
}

/* ==== METODOS DO SENSOR ULTRASONICO ==== */

// EMITE UM SINAL 
void envia(int trigger) {
  fastDigitalWrite(trigger, LOW);
  tempoDeEspera(2);
  fastDigitalWrite(trigger, HIGH);
  tempoDeEspera(10);
  fastDigitalWrite(trigger, LOW);
}

// RETORNA O VALOR EM CM DE ACORDO COM O TEMPO DE DURAÇÃO DE UMA RESPOSTA
long recebe(int echo) {
  long duracao, cm;
  duracao = pulseIn(echo, HIGH);
  cm = microsegundosParaCentimetros(duracao);
  return cm;
}

// VERIFICA O TEMPO QUE DEMOROU PARA A CHEGADA DE UMA RESPOSTA
void tempoDeEspera(int val) {
  unsigned long a = micros();
  unsigned long b = micros();
  while ((b - a) < val) {
    b = micros();
    if (a > b) {
      break;
    }
  }
}

// CALCULA E RETORNA UMA DISTANCIA DE ACORDO COM UMA TRIGGER E UM ECHO
long medirDistancia(int trigger, int echo) {
  long resultado;
  envia(trigger);
  resultado = recebe(echo);
  return resultado;
}

// CONVERTE O VALOR DE MICROSEGUNDOS PARA CENTIMETROS
long microsegundosParaCentimetros(long microsegundos) {
  return microsegundos / 29 / 2;
}


/* ==== METODOS PARA RFID ==== */

// 1. VERIFICA SE TEM UMA ETIQUETA PROXIMA DO LEITOR SUA LEITURA E RETORNA UID (DIGITO DE IDENTIFICACAO DO USUÁRIO)
String getUid() {
    String uid ="";
    if(rfid.isCard() && rfid.readCardSerial()) {
    for(int i = 0; i < sizeof(rfid); i++) {
        uid += rfid.serNum[i];
        uid = uid.substring(0,4);
      }  
       return uid;
  }  
}
