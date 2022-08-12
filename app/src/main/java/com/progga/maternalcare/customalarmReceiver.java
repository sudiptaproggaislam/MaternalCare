package com.progga.maternalcare;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class customalarmReceiver extends BroadcastReceiver {
    int cnt=0;
    String s="abc";
    String finddate="6521562591368123";
    String msgkey,msg,message;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat dateFormat1 = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
    SimpleDateFormat dateFormat2 = new SimpleDateFormat("EEE, d MMM yyyy");
    String stringdate1;
    long pDate = 0;
    long date1 = 0;
    long date2 = 0;
    @Override
    public void onReceive(Context context, Intent intent) {
        DatabaseReference ref= FirebaseDatabase.getInstance("https://maternal-care-fa0e8-default-rtdb.firebaseio.com/").getReference();
        FirebaseAuth dAuth=FirebaseAuth.getInstance();
        String currentUserId=dAuth.getCurrentUser().getUid();
        String stringReceiverEmail=dAuth.getCurrentUser().getEmail();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Date currentDate = new Date();
                stringdate1 = dateFormat.format(currentDate);

                try {
                    Date cdate = dateFormat.parse(stringdate1);
                    date1 = cdate.getTime();
//                    date2= date1 + 86400000;
//                    System.out.println("cdate abc " + date1);
                } catch (ParseException e) {
                    System.out.println("error date " + e);
                }

                Iterator iterator=snapshot.child("customreminder").child(currentUserId).getChildren().iterator();
                while (iterator.hasNext()){
                    String findkey=((DataSnapshot)iterator.next()).getKey();
                    finddate=snapshot.child("customreminder").child(currentUserId).child(findkey).child("date").getValue().toString();
                    if (Long.parseLong(finddate) == date1) {
                        msgkey=findkey;
                        s=finddate;
                        pDate = Long.parseLong(s);
                        message=snapshot.child("customreminder").child(currentUserId).child(msgkey).child("msg").getValue().toString();
                        break;
                    } else {
                        System.out.println("notifying2");
                    }

                }
//                for (DataSnapshot ds : snapshot.child("customreminder").child(currentUserId).getChildren()) {
//                    if (Long.parseLong(ds.getValue().toString()) == date1) {
//                        s = ds.getValue().toString();
//                        msgkey=ds.getKey().toString();
//                        pDate = Long.parseLong(s);
//
//                        break;
//                    } else {
//                        System.out.println("notifying2");
//                    }
//                }

                if (s == "abc") {
                    System.out.println("no date");
                } else {
//                    String c= snapshot.child("customreminderkey").child(msgkey).child("cnt").getValue().toString();
//                    msg=snapshot.child("customreminderkey").child(msgkey).child("msg").getValue().toString();
//                    cnt = Integer.parseInt(c);
//                    if (cnt == 1) {
                        System.out.println("success 123");
                        Date cd = new Date(pDate);

                        Intent i = new Intent(context, showcustomremindermsg.class);
                        i.putExtra("date", s);
                        i.putExtra("msg", message);
                        i.putExtra("id", currentUserId);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, i, PendingIntent.FLAG_UPDATE_CURRENT);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "customreminder")
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setContentTitle("Maternal Care")
                                .setContentText("Reminder for: "+message +"\n"+ dateFormat2.format(cd))
                                .setAutoCancel(true)
                                .setDefaults(NotificationCompat.DEFAULT_ALL)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setContentIntent(pendingIntent);

                        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
                        notificationManagerCompat.notify(123, builder.build());

                        DatabaseReference refUpdate = FirebaseDatabase.getInstance("https://maternal-care-fa0e8-default-rtdb.firebaseio.com/").getReference();
                        refUpdate.child("customreminderkey").child(msgkey).child("cnt").setValue(Integer.toString(cnt + 1)).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    DatabaseReference ref1 = FirebaseDatabase.getInstance("https://maternal-care-fa0e8-default-rtdb.firebaseio.com/").getReference();

                                    String num = snapshot.child("User").child(currentUserId).child("rnotification").getValue().toString();
                                    int nm = Integer.parseInt(num);
                                    DatabaseReference ref2 = FirebaseDatabase.getInstance("https://maternal-care-fa0e8-default-rtdb.firebaseio.com/").getReference();
                                    ref2.child("User").child(currentUserId).child("rnotification").setValue(Integer.toString(nm + 1));
                                    DatabaseReference ref3 = FirebaseDatabase.getInstance("https://maternal-care-fa0e8-default-rtdb.firebaseio.com/").getReference();
                                    final HashMap<String, String> profileMap = new HashMap<>();
                                    Date cDateUp = new Date();
                                    Date rDateUp = new Date(date1);
                                    profileMap.put("key", "2");
                                    profileMap.put("ctime", dateFormat1.format(cDateUp));
                                    profileMap.put("rtime", s);
                                    profileMap.put("msg", message);
                                    ref3.child("notification").child(currentUserId).push().setValue(profileMap);
                                    sendMail(stringReceiverEmail, dateFormat.format(rDateUp), message);
                                    DatabaseReference refDelete = FirebaseDatabase.getInstance("https://maternal-care-fa0e8-default-rtdb.firebaseio.com/").getReference();
                                    refDelete.child("customreminder").child(currentUserId).child(msgkey).removeValue();


//                                ref1.child("User").child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                    }
//                                });
                                    System.out.println("Send");
//                                ref.removeEventListener(listener);


                                } else {
                                    System.out.println(task.getException().toString());
                                }
                            }
                        });

//                    } else {
//                        System.out.println("notifying");
//                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }








    void sendMail(String stringReceiverEmail,String visitDate,String messagemail){
        try {
            String stringSenderEmail = "workmailprogga@gmail.com";
            String stringPasswordSenderEmail = "szmkkgbrlidrymhk";

            String stringHost = "smtp.gmail.com";

            Properties properties = System.getProperties();

            properties.put("mail.smtp.host", stringHost);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");

            javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));

            mimeMessage.setSubject("Maternal Care");
            mimeMessage.setText("Reminder for: "+messagemail +"\nDate : "+ visitDate);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(mimeMessage);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
