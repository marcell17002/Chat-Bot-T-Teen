
package com.chatbot.translate;
import com.google.gson.Gson;
import com.linecorp.bot.client.LineMessagingServiceBuilder;
import com.linecorp.bot.client.LineSignatureValidator;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import retrofit2.Response;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.io.IOException;

@RestController
@RequestMapping(value="/linebot")
public class LineBotController
{
    @Autowired
    @Qualifier("com.linecorp.channel_secret")
    String lChannelSecret;

    @Autowired
    @Qualifier("com.linecorp.channel_access_token")
    String lChannelAccessToken;

    private static final String CLIENT_ID = "FREE_TRIAL_ACCOUNT";
    private static final String CLIENT_SECRET = "PUBLIC_SECRET";
    private static final String ENDPOINT = "http://api.whatsmate.net/v1/translation/translate";

    @RequestMapping(value="/callback", method=RequestMethod.POST)

    public ResponseEntity<String> callback(
            @RequestHeader("X-Line-Signature") String aXLineSignature,
            @RequestBody String aPayload) throws IOException {
        final String text=String.format("The Signature is: %s",
                (aXLineSignature!=null && aXLineSignature.length() > 0) ? aXLineSignature : "N/A");
        System.out.println(text);
        final boolean valid=new LineSignatureValidator(lChannelSecret.getBytes()).validateSignature(aPayload.getBytes(), aXLineSignature);
        System.out.println("The signature is: " + (valid ? "valid" : "tidak valid"));
        if(aPayload!=null && aPayload.length() > 0)
        {
            System.out.println("Payload: " + aPayload);
        }
        Gson gson = new Gson();
        Payload payload = gson.fromJson(aPayload, Payload.class);

        String msgText = " ";
        String idTarget = " ";
        String eventType = payload.events[0].type;

        if (eventType.equals("join")){
            if (payload.events[0].source.type.equals("group")){
                replyToUser(payload.events[0].replyToken, "Hello Group");
            }
            if (payload.events[0].source.type.equals("room")){
                replyToUser(payload.events[0].replyToken, "Hello Room");
            }
        } else if (eventType.equals("message")){
            if (payload.events[0].source.type.equals("group")){
                idTarget = payload.events[0].source.groupId;
            } else if (payload.events[0].source.type.equals("room")){
                idTarget = payload.events[0].source.roomId;
            } else if (payload.events[0].source.type.equals("user")){
                idTarget = payload.events[0].source.userId;
            }

            if (!payload.events[0].message.type.equals("text")){
                replyToUser(payload.events[0].replyToken, "Unknown message");
            } else {
                msgText = payload.events[0].message.text;
                msgText = msgText.toLowerCase();

                if (!msgText.contains("bot leave")){
                    if(msgText.contains("Siapa yang membuat mu ? ")){

                        String balas = "created : Marcell Antonius dan Felia Sri Indriyani";
                        replyToUser(payload.events[0].replyToken, balas);
                    }
                    if(msgText.contains("hi")){

                        String balas = "Hi , Kenalin aku T-Teen aku akan membantumu untuk merekomendasikan menu makananan hari ini yang sesuai dengan mood mu hari ini . Boleh aku tahu bagaimanakah perasaanmu hari ini ? ";
                        replyToUser(payload.events[0].replyToken, balas);
                    }
                    if(msgText.contains("Sedih")){

                        String balas = "Jika kamu sedang sedih, aku merekomendasikanmu makan makanan yang kaya akan Omega-3. Tujuannya adalah untuk meningkatkan moodmu hari ini  serta mengatasi tekanan agar tidak semakin larut dalam kesedihan . Makanan yang kaya Omega-3 antara lain: ikan-ikanan, telur, daging merah . Nah , dari pilihan itu kamu pilih yang mana ? ";
                        replyToUser(payload.events[0].replyToken, balas);
                    }
                    if(msgText.contains("Marah")){

                        String balas = "Saat sedang marah jangan biarkan emosi Anda meledak-ledak dan menguasai. Hasilnya akan berbahaya lho. Mengatasi hal ini lebih baik Anda menenangkan diri dengan mengonsumsi makanan yang kaya akan zinc. Menurut penelitian di Jepang, seperti dikutip dari ivillage.co.uk, zinc dapat membantu mengendalikan emosi dan meredakan kemarahan. Makanan yang kaya zinc antara lain: daging merah, ayam, produk susu, makanan laut, gandum-ganduman, kacang-kacangan. Nah , dari pilihan itu kamu pilih yang mana ?"    ;
                        replyToUser(payload.events[0].replyToken, balas);
                    }
                    if(msgText.contains("Jatuh cinta")){

                        String balas = "Sedang jatuh cinta dan berbunga-bunga? Ada beberapa makanan yang akan membuatmu semakin ceria dan menambah gairah asmara cinta. Makanan yang harus diasup adalah yang mengandung zinc, arginine, selenium dan niacin. Makanan ini bisa disebut juga sebagai afrodisiak. Membangkitkan gairah dalam hal cinta serta seksual. Di antara lain seperti: kuaci labu, tiram, cabai, cokelat, daging kambing, alpukat, ikan-ikanan, dan lain sebagainya. Nah , dari pilihan itu kamu pilih yang mana ?";
                        replyToUser(payload.events[0].replyToken, balas);
                    }
                    if(msgText.contains("Ga pede")){

                        String balas = "Kalau sedang tak percaya diri ada beberapa makanan yang bisa disantap untuk meningkatkan kepercayaan diri Anda.Para peneliti di McGill University Monteral mengatakan bahwa mengatasi kurang percaya diri bisa dengan mengonsumsi makanan yang mengandung tryptophan. Makanan yang menjadi sumber tryptophan antara lain: daging (terutama ayam), ikan (terutama salmon dan tuna). Nah , dari pilihan itu kamu pilih yang mana ?";
                        replyToUser(payload.events[0].replyToken, balas);
                    }
                    if(msgText.contains("Ikan")) {

                        String balas = "Nah berikut ini adalah menu makananan hasil rekomendasiku untuk makanan yang kamu pilih . ikan bakar , ikan goreng , ikan sayur . Kamu mau pilih yang mana ? ";
                        replyToUser(payload.events[0].replyToken, balas);
                    }

                    if(msgText.contains("Daging merah")){

                        String balas = "Nah berikut ini adalah menu makananan hasil rekomendasiku untuk makanan yang kamu pilih . Tenderloin , sop buntut  . Kamu mau pilih yang mana ? ";
                        replyToUser(payload.events[0].replyToken, balas);
                    }
                    if(msgText.contains("Sop buntut")){

                        String balas = "kamu bisa pergi ke tempat makan berikut . Kantin Pedca , Kantin FIB dan Kantin Psikologi disana meneyediakan makanan yang kamu pilih . :))";
                        replyToUser(payload.events[0].replyToken, balas);
                    }
                    if(msgText.contains("Daging kambing")){

                        String balas = "Nah berikut ini adalah menu makananan hasil rekomendasiku untuk makanan yang kamu pilih . sop iga kambing , iga kambing bakar . Kamu mau pilih yang mana ? ";
                        replyToUser(payload.events[0].replyToken, balas);
                    }
                    if(msgText.contains("Ayam")){

                        String balas = "Nah berikut ini adalah menu makananan hasil rekomendasiku untuk makanan yang kamu pilih . ayam goreng , ayam geprek, ayam rica-rica, ayam sayur, dan ayam karedok . Kamu mau pilih yang mana ";
                        replyToUser(payload.events[0].replyToken, balas);
                    }
                    if(msgText.contains("Pecel sayur")) {

                        String balas = "kamu bisa pergi ke tempat makan berikut . Kantin Pedca disana meneyediakan makanan yang kamu pilih . :))";
                        replyToUser(payload.events[0].replyToken, balas);
                    }
                    if(msgText.contains("Karedok")){

                        String balas = "kamu bisa pergi ke tempat makan berikut . Kantin PPBS disana meneyediakan makanan yang kamu pilih . :))";
                        replyToUser(payload.events[0].replyToken, balas);
                    }
                    if(msgText.contains("Telur balado")){

                        String balas = "kamu bisa pergi ke tempat makan berikut . Kantin Pedca , Kantin FK dan Kantin Statistika disana meneyediakan makanan yang kamu pilih . :))";
                        replyToUser(payload.events[0].replyToken, balas);
                    }
                    if(msgText.contains("Ikan sayur")){

                        String balas = "kamu bisa pergi ke tempat makan berikut . Kantin Pedca , Kantin FK dan Cantina MIPA disana menyediakan makanan yang kamu pilih . :))";
                        replyToUser(payload.events[0].replyToken, balas);
                    }
                    if(msgText.contains("Ikan goreng")){

                        String balas = "kamu bisa pergi ke tempat makan berikut . Kantin Pedca, Kantin FK dan Kantin Statistika disana menyediakan makanan yang kamu pilih . :)) ";
                        replyToUser(payload.events[0].replyToken, balas);
                    }
                    if(msgText.contains("Ikan bakar")){

                        String balas = "kamu bisa pergi ke tempat makan berikut . Kantin Bale , Kantin FK dan Kantin Psikologi disana menyediakan makanan yang kamu pilih . :))";
                        replyToUser(payload.events[0].replyToken, balas);
                    }
                    if(msgText.contains("Tenderloin")){

                        String balas = "kamu bisa pergi ke tempat makan berikut . Kantin FK disana menyediakan makanan yang kamu pilih . :) ";
                        replyToUser(payload.events[0].replyToken, balas);
                    }
                    if(msgText.contains("Sop iga kambing")){

                        String balas = "kamu bisa pergi ke tempat makan berikut . Kantin Pedca , Kantin FIB dan Kantin Psikologi disana menyediakan makanan yang kamu pilih . :)";
                        replyToUser(payload.events[0].replyToken, balas);
                    }
                    if(msgText.contains("Sop buntut")){

                        String balas = "kamu bisa pergi ke tempat makan berikut . Kantin Pedca , Kantin FIB dan Kantin Psikologi disana menyediakan makanan yang kamu pilih . :))";
                        replyToUser(payload.events[0].replyToken, balas);
                    }
                    if(msgText.contains("Ayam karedok")){

                        String balas = "kamu bisa pergi ke tempat makan berikut . Kantin FK , Kantin FIB, Kantin PPBS dan Kantin Psikologi disana menyediakan makanan yang kamu pilih . :)";
                        replyToUser(payload.events[0].replyToken, balas);
                    }
                    if(msgText.contains("Ayam sayur")){

                        String balas = "kamu bisa pergi ke tempat makan berikut . Kantin Pedca , Kantin FIB, dan Kantin Statistika disana menyediakan makanan yang kamu pilih . :)";
                        replyToUser(payload.events[0].replyToken, balas);
                    }
                    if(msgText.contains("Ayam rica-rica")){

                        String balas = "kamu bisa pergi ke tempat makan berikut . Kantin Pedca , Kantin Statistika, dan Kantin Pertanian disana menyediakan makanan yang kamu pilih . :)";
                        replyToUser(payload.events[0].replyToken, balas);
                    }
                    if(msgText.contains("Ayam geprek")){

                        String balas = "kamu bisa pergi ke tempat makan berikut . Kantin Pedca , Kantin PPBS dan Kantin Bale Wilasa 7 disana menyediakan makanan yang kamu pilih . :) ";
                        replyToUser(payload.events[0].replyToken, balas);
                    }
                    if(msgText.contains("Kacang")){

                        String balas = "Nah berikut ini adalah menu makananan hasil rekomendasiku untuk makanan yang kamu pilih . karedok , pecel sayur . Kamu mau pilih yang mana ? ";
                        replyToUser(payload.events[0].replyToken, balas);
                    }
                    if(msgText.contains("Telur")){

                        String balas = "Nah berikut ini adalah menu makananan hasil rekomendasiku untuk makanan yang kamu pilih . telur dadar , telur balado  . Kamu mau pilih yang mana ? ";
                        replyToUser(payload.events[0].replyToken, balas);
                    }
                } else {
                    String balas = "Mohon maaf fitur ini akan terus diperbaiki untuk menunjang kenyamanan anda sekalian :))" ;
                    replyToUser(payload.events[0].replyToken, balas);
                }

            }
        }

        return new ResponseEntity<String>(HttpStatus.OK);
    }

//    private void translate(String fromLang, String toLang, String text, String payload) throws IOException {
//        // TODO: Should have used a 3rd party library to make a JSON string from an object
//        String jsonPayload = new StringBuilder()
//                .append("{")
//                .append("\"fromLang\":\"")
//                .append(fromLang)
//                .append("\",")
//                .append("\"toLang\":\"")
//                .append(toLang)
//                .append("\",")
//                .append("\"text\":\"")
//                .append(text)
//                .append("\"")
//                .append("}")
//                .toString();
//
//        URL url = new URL(ENDPOINT);
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setDoOutput(true);
//        conn.setRequestMethod("POST");
//        conn.setRequestProperty("X-WM-CLIENT-ID", CLIENT_ID);
//        conn.setRequestProperty("X-WM-CLIENT-SECRET", CLIENT_SECRET);
//        conn.setRequestProperty("Content-Type", "application/json");
//
//        OutputStream os = conn.getOutputStream();
//        os.write(jsonPayload.getBytes());
//        os.flush();
//        os.close();
//
//        int statusCode = conn.getResponseCode();
//        System.out.println("Status Code: " + statusCode);
//        BufferedReader br = new BufferedReader(new InputStreamReader(
//                (statusCode == 200) ? conn.getInputStream() : conn.getErrorStream()
//        ));
//        String output;
//        while ((output = br.readLine()) != null) {
//            replyToUser(payload, output);
//            //System.out.println(output);
//        }
//        conn.disconnect();
//    }

    private void getMessageData(String message, String targetID) throws IOException{
        if (message!=null){
            pushMessage(targetID, message);
        }
    }

    private void replyToUser(String rToken, String messageToUser){
        TextMessage textMessage = new TextMessage(messageToUser);
        ReplyMessage replyMessage = new ReplyMessage(rToken, textMessage);
        try {
            Response<BotApiResponse> response = LineMessagingServiceBuilder
                    .create(lChannelAccessToken)
                    .build()
                    .replyMessage(replyMessage)
                    .execute();
            System.out.println("Reply Message: " + response.code() + " " + response.message());
        } catch (IOException e) {
            System.out.println("Exception is raised ");
            e.printStackTrace();
        }
    }

    private void pushMessage(String sourceId, String txt){
        TextMessage textMessage = new TextMessage(txt);
        PushMessage pushMessage = new PushMessage(sourceId,textMessage);
        try {
            Response<BotApiResponse> response = LineMessagingServiceBuilder
                    .create(lChannelAccessToken)
                    .build()
                    .pushMessage(pushMessage)
                    .execute();
            System.out.println(response.code() + " " + response.message());
        } catch (IOException e) {
            System.out.println("Exception is raised ");
            e.printStackTrace();
        }
    }

    private void leaveGR(String id, String type){
        try {
            if (type.equals("group")){
                Response<BotApiResponse> response = LineMessagingServiceBuilder
                        .create(lChannelAccessToken)
                        .build()
                        .leaveGroup(id)
                        .execute();
                System.out.println(response.code() + " " + response.message());
            } else if (type.equals("room")){
                Response<BotApiResponse> response = LineMessagingServiceBuilder
                        .create(lChannelAccessToken)
                        .build()
                        .leaveRoom(id)
                        .execute();
                System.out.println(response.code() + " " + response.message());
            }
        } catch (IOException e) {
            System.out.println("Exception is raised ");
            e.printStackTrace();
        }
    }
}
