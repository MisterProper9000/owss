#include "IRremote.h"
#include <SoftwareSerial.h>
// библиотека для работы программного Serial
#define SPEED_1      5
#define DIR_1        4

#define SPEED_2      6
#define DIR_2        7

#define IR           8

#define LED          9
//#define reservedTimeOut 50000
#define WIFISERIAL   Serial2

//String isReserved;
//int reservedTimeOut = 8;

String sender = "GET /ardgetstatus HTTP/1.1\nHost: 10.101.177.12:9091\n\n";
const char* ssid     = "OW-Summer";      // The SSID (name) of the Wi-Fi network you want to connect to
const char* password = "Summer20!9";     // The password of the Wi-Fi network
String ip = "10.101.177.12";
String port = "9091";

IRrecv irrecv(IR); // указываем вывод, к которому подключен приемник
decode_results results;
short int speed = 100;
bool isRent = false;
bool timeToStop = false;
bool alert = false;
bool isStop = true;

String wifiSend(String msg, const int timeout)
{
  String responce = "";
  WIFISERIAL.println(msg);
  long int time = millis();
  while ((time + timeout) > millis())
  {
    while (WIFISERIAL.available())
    {
      char c = WIFISERIAL.read();
      responce += c;
    }

  }
  return responce;
}

String resp;
int i = 0;
//int resCoolUp = 0;
void setup() {
  Serial.begin(9600); // выставляем скорость COM порта
  while (!Serial) {
    // ждём, пока не откроется монитор последовательного порта
    // для того, чтобы отследить все события в программе
  }
  i = 0;
  //resCoolUp = 0;
  //isReserved = "0";
  /*---------------WI-FI--setup---------------*/
  WIFISERIAL.begin(115200);
  while (!WIFISERIAL) {
    ;
  }
  //reservedTimeOut = 50000;

  resp = wifiSend("AT+CWLAP", 5000);
  Serial.println(resp);
  resp = wifiSend("AT+CIPSTATUS", 1000);
  Serial.println((String)"0 " + resp);
  String str = (String)"AT+CWJAP=\"" + ssid + (String)"\",\"" + password + (String)"\"";
  resp = wifiSend(str, 1000);
  Serial.println((String)"1 " + resp);
  resp = wifiSend(str, 2000);
  Serial.println((String)"2 " + resp);
  delay(2000);
  resp = wifiSend("AT+CIPSTART=\"TCP\",\"10.101.177.12\",9091", 1000);
  Serial.println((String)"3 " + resp);
  delay(2000);
  resp = wifiSend((String)"AT+CIPSEND=" + sender.length(), 1000);
  Serial.println((String)"4 " + resp);
  delay(2000);
  resp = wifiSend(sender, 1000);
  Serial.println((String)"5 " + resp);
  Serial.println("--");
  /*------------------------------------------*/

  irrecv.enableIRIn(); // запускаем прием
  // настраиваем выводы платы 4, 5, 6, 7 на вывод сигналов
  for (int i = 4; i < 8; i++) {
    pinMode(i, OUTPUT);
  }
  pinMode(LED, OUTPUT);

  digitalWrite(LED, HIGH);   // зажигаем светодиод
  delay(100);              // ждем
  digitalWrite(LED, LOW);    // выключаем светодиод
  delay(100);
  digitalWrite(LED, HIGH);   // зажигаем светодиод
  delay(100);              // ждем
  digitalWrite(LED, LOW);    // выключаем светодиод
  Serial.println("ready");

}

/*DIR2 -- правый
  DIR1 -- левый
  DIR1 LOW -- вперёд
  DIR2 HIGH -- вперёд
*/
void forward()
{
  // устанавливаем направление мотора «M1» в одну сторону
  digitalWrite(DIR_1, LOW);
  // включаем мотор на максимальной скорости
  analogWrite(SPEED_1, speed);
  // устанавливаем направление мотора «M2» в одну сторону
  digitalWrite(DIR_2, HIGH);
  // включаем второй мотор на максимальной скорости
  analogWrite(SPEED_2, speed);
}

void back()
{
  digitalWrite(DIR_1, HIGH);
  analogWrite(SPEED_1, speed);
  digitalWrite(DIR_2, LOW);
  analogWrite(SPEED_2, speed);
}

void left()
{
  digitalWrite(DIR_1, LOW);
  analogWrite(SPEED_1, speed);
  digitalWrite(DIR_2, LOW);
  analogWrite(SPEED_2, speed);
}

void right()
{
  digitalWrite(DIR_1, HIGH);
  analogWrite(SPEED_1, speed);
  digitalWrite(DIR_2, HIGH);
  analogWrite(SPEED_2, speed);
}

void forwardright()
{
  digitalWrite(DIR_2, HIGH);
  analogWrite(SPEED_2, speed);
  analogWrite(SPEED_1, 0);
}

void forwardleft()
{
  digitalWrite(DIR_1, LOW);
  analogWrite(SPEED_1, speed);
  analogWrite(SPEED_2, 0);
}

void backright()
{
  digitalWrite(DIR_2, LOW);
  analogWrite(SPEED_2, speed);
  analogWrite(SPEED_1, 0);
}

void backleft()
{
  digitalWrite(DIR_1, HIGH);
  analogWrite(SPEED_1, speed);
  analogWrite(SPEED_2, 0);
}

void Stop()
{
  analogWrite(SPEED_1, 0);
  analogWrite(SPEED_2, 0);
}


String parseResp(String msg)
{
  for (int i = 0; i < msg.length() - 3; i++)
  {
    if (msg.charAt(i) == 'a' && msg.charAt(i + 1) == 'l' && msg.charAt(i + 2) == 's' && msg.charAt(i + 3) == 'e')
    {
      if (isRent)
      {
        timeToStop = true;
        digitalWrite(LED, HIGH);   // зажигаем светодиод
        delay(100);              // ждем
        digitalWrite(LED, LOW);    // выключаем светодиод
      }
      if (isRent && isStop)
        timeToStop = false;
      isRent = false;
      return "kek";
    }
    if (msg.charAt(i) == 't' && msg.charAt(i + 1) == 'r' && msg.charAt(i + 2) == 'u' && msg.charAt(i + 3) == 'e')
    {
      if (!isRent)
      {
        digitalWrite(LED, HIGH);   // зажигаем светодиод
        delay(100);              // ждем
        digitalWrite(LED, LOW);    // выключаем светодиод
        timeToStop = false;
       // isReserved = "0";
       // resCoolUp = 0;
      }
      
      isRent = true;
      return "sas";
    }
    if (msg.charAt(i) == 'r' && msg.charAt(i + 1) == 'e' && msg.charAt(i + 2) == 's' && msg.charAt(i + 3) == '1') //бронирование
    {
      Serial.println("ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
      digitalWrite(LED, HIGH);   // зажигаем светодиод
      delay(100);              // ждем
      digitalWrite(LED, LOW);    // выключаем светодиод
     //  Serial.print("sas ");
    //Serial.println(isReserved);
      //if (isReserved.equals("0"))
     // {
     //   Serial.print("sas ");
     //   isReserved = "1";
     //   resCoolUp = 0;
     // }
    }
    if ((msg.charAt(i) == 'R' && msg.charAt(i + 1) == 'R' && msg.charAt(i + 2) == 'O' && msg.charAt(i + 3) == 'R') //переподключение
        || (msg.charAt(i) == 'L' && msg.charAt(i + 1) == 'O' && msg.charAt(i + 2) == 'S' && msg.charAt(i + 3) == 'E'))
    {
      Serial.println("sssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
      digitalWrite(LED, HIGH);   // зажигаем светодиод
      delay(100);              // ждем
      digitalWrite(LED, LOW);    // выключаем светодиод
      String str = (String)"AT+CWJAP=\"" + ssid + (String)"\",\"" + password + (String)"\"";
      resp = wifiSend(str, 1000);
      Serial.println((String)"1 " + resp);
      resp = wifiSend(str, 2000);
      Serial.println((String)"2 " + resp);
      delay(2000);
      resp = wifiSend("AT+CIPSTART=\"TCP\",\"10.101.177.12\",9091", 1000);
      Serial.println((String)"3 " + resp);
      resp = wifiSend((String)"AT+CIPSEND=" + sender.length(), 1000);
      Serial.println((String)"4 " + resp);
      delay(2000);
      resp = wifiSend(sender, 1000);
      digitalWrite(LED, HIGH);   // зажигаем светодиод
      delay(100);              // ждем
      digitalWrite(LED, LOW);    // выключаем светодиод
    }
  }
}

void loop() {
  
 /* if (resCoolUp > reservedTimeOut)
  {
    
    Serial.println("reserve ended");
      wifiSend((String)"AT+CIPSEND=" + sender1.length(), 500);
      resp = wifiSend(sender1, 500);
      Serial.println(resp);
      parseResp(resp);
      //isReserved = "0";
      resCoolUp = 0;
      //надо отправить серверу сообщение, что бронь истекла
  }*/

  while (Serial.available()) {
  WIFISERIAL.write(Serial.read());
  }
  while (WIFISERIAL.available()) {
  Serial.write(WIFISERIAL.read());
  }
  i++;
  //Serial.println(i);
  if (i % 12000 == 0) //очень тупа
{
  
    wifiSend((String)"AT+CIPSEND=" + sender.length(), 500);
    resp = wifiSend(sender, 500);
    Serial.println(resp);
    parseResp(resp);
  }

  if ( irrecv.decode( &results )) { // если данные пришли
  Serial.println( results.value, HEX ); // печатаем данные
    if (!isRent && !timeToStop)
    {
      digitalWrite(LED, HIGH);
      alert = true;
    }
    else
    {
      isStop = false;
      digitalWrite(LED, LOW);
      switch ( results.value ) {
        case 0x1689609F://вперёд
          forward();
          break;
        case 0x1689B847://назад
          back();
          break;
        case 0x16899867://право (на месте)
          right();
          break;
        case 0x168910EF://лево (на месте)
          left();
          break;
        case 0x168958A7://прямо-право
          forwardright();
          break;
        case 0x168950AF://прямо-лево
          forwardleft();
          break;
        case 0x168920DF://назад право
          backright();
          break;
        case 0x168918E7://назад-лево
          backleft();
          break;
        case 0x168938C7://стоп-старт
          isStop = true;
          Stop();
          if (!isRent)
            timeToStop = false;
          break;
      }
    }

    irrecv.resume(); // принимаем следующую команду
  }
  if (!isRent && alert)
{
  digitalWrite(LED, HIGH);   // зажигаем светодиод
    delay(100);              // ждем
    digitalWrite(LED, LOW);    // выключаем светодиод
    delay(100);
    digitalWrite(LED, HIGH);   // зажигаем светодиод
    delay(100);              // ждем
    digitalWrite(LED, LOW);    // выключаем светодиод
    delay(100);
    digitalWrite(LED, HIGH);   // зажигаем светодиод
    delay(100);              // ждем
    digitalWrite(LED, LOW);    // выключаем светодиод
    delay(100);
    digitalWrite(LED, HIGH);   // зажигаем светодиод
    delay(100);              // ждем
    digitalWrite(LED, LOW);    // выключаем светодиод
    delay(100);
    alert = false;
  }
}
