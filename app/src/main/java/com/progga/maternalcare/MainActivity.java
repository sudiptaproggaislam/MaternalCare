package com.progga.maternalcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    private Toolbar mToolbar;
    ActionBarDrawerToggle toggle;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference ref,refAdminAccess;
    public String UserStatus,stUserName,category,currentUserId,currentUserGmail,stcounter,ncounter;

    private TextView tvUserName,tvUserEmail,counter,tvStatusView,tvStatusmsg,name,ncnt;
    private LinearLayout dueDate,routineVisit,childVaccination,customReminder,btnQueries,btnBlogs, statusView,msgView,notification;
    private CircleImageView cvPregnancyWbw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialization();

        mAuth=FirebaseAuth.getInstance();
        ref= FirebaseDatabase.getInstance("https://maternal-care-fa0e8-default-rtdb.firebaseio.com/").getReference();
        currentUser=mAuth.getCurrentUser();

        mToolbar=(Toolbar)findViewById(R.id.main_activity_toolbar);
        drawerLayout= findViewById(R.id.drawer);
        navigationView=findViewById(R.id.navigationView);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Maternal Care");
        toggle=new ActionBarDrawerToggle(this,drawerLayout,mToolbar,R.string.drawerOpen,R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    protected void onStart() {
        super.onStart();

        if (currentUser != null && currentUser.isEmailVerified()) {
            VerifyUserExistance();
        } else {
            LoginActivity();
        }
    }

    private void initialization() {
        dueDate=(LinearLayout)findViewById(R.id.btnDueDate);
        routineVisit=(LinearLayout)findViewById(R.id.btnRoutineVisit);
        childVaccination=(LinearLayout)findViewById(R.id.btnChildVaccination);
        customReminder=(LinearLayout)findViewById(R.id.btnCustomReminder);
        btnQueries=(LinearLayout)findViewById(R.id.btnQuery);
        btnBlogs=(LinearLayout)findViewById(R.id.btnBlog);
        notification=(LinearLayout)findViewById(R.id.btnReminderNotification);

        statusView=(LinearLayout)findViewById(R.id.lvStatus);
        msgView=(LinearLayout)findViewById(R.id.lvMsg);
        counter=(TextView)findViewById(R.id.tvcounter);
        tvStatusmsg=(TextView)findViewById(R.id.statusMsg);
        name=(TextView)findViewById(R.id.tvUserNameNew);        //cvPregnancyWbw=(CircleImageView) findViewById(R.id.cvPregnancystatus);
        ncnt=(TextView) findViewById(R.id.tvcounterNotification);
    }

    private void VerifyUserExistance() {
        currentUserId=mAuth.getCurrentUser().getUid();
        currentUserGmail=mAuth.getCurrentUser().getEmail();

        ///update
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("User").child(currentUserId).child("name").exists()){
                    stUserName = dataSnapshot.child("User").child(currentUserId).child("name").getValue().toString();
                    stcounter = dataSnapshot.child("User").child(currentUserId).child("notification").getValue().toString();
                    ncounter= dataSnapshot.child("User").child(currentUserId).child("rnotification").getValue().toString();

                    //routine
                    if (stcounter.equals("0")){
                        counter.setVisibility(View.INVISIBLE);
                    }else{
//                        finish();
//                        startActivity(getIntent());
                        counter.setText(stcounter);
                    }
                    //notification
                    if (ncounter.equals("0")){
                        ncnt.setVisibility(View.INVISIBLE);
                    }else{
                        ncnt.setText(ncounter);
                    }

                    //msg bar
                    if(dataSnapshot.child("dueReminder").child(currentUserId).exists()){
                        msgView.removeAllViews();
                        String ld=dataSnapshot.child("User").child(currentUserId).child("LastPeriod").getValue().toString();
                        setCurrentStatus(ld);
                    }else{
                        statusView.removeAllViews();
                    }


                    name.setText(stUserName);

                    dueDate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent loginIntent= new Intent(MainActivity.this,dueDateActivity.class);
                            startActivity(loginIntent);
                        }
                    });
                    childVaccination.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent immunization= new Intent(MainActivity.this,immunization_schedule.class);
                            startActivity(immunization);
                        }
                    });
                    routineVisit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent alarmNotificationIntent= new Intent(MainActivity.this,alarmNotification.class);
                            startActivity(alarmNotificationIntent);
                        }
                    });

                    btnQueries.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent queryIntent= new Intent(MainActivity.this,queryActivity.class);
                            startActivity(queryIntent);
                        }
                    });

                    btnBlogs.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent blogIntent= new Intent(MainActivity.this,blog_activity.class);
                            startActivity(blogIntent);
                        }
                    });
                    customReminder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent blogIntent= new Intent(MainActivity.this,customreminderActivity.class);
                            startActivity(blogIntent);
                        }
                    });
                    notification.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent blogIntent= new Intent(MainActivity.this,notificationActivity.class);
                            startActivity(blogIntent);
                        }
                    });

                }else{
                    SendUserToSettingsActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setCurrentStatus(String ld) {
        long ldate=Long.parseLong(ld);
        long crntdate=0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        String stringdate = dateFormat.format(currentDate);

        try {
            Date cdate= dateFormat.parse(stringdate);
            crntdate = cdate.getTime();
        } catch (ParseException e) {
            System.out.println("error date "+ e);
        }

        long diff=((crntdate-ldate)/(7*86400*1000))+1;

        if(diff==1){
            tvStatusmsg.setText("Current Status: Your baby has yet to be conceived.");
        }
        else if(diff ==2){
            tvStatusmsg.setText("Current Status: It's a potentially life-changing week. You'll ovulate, and if egg meets sperm, you'll be on your way to pregnancy!");
        }
        else if(diff ==3){
            tvStatusmsg.setText("Current Status: Your baby is a tiny ball – called a blastocyst – made up of several hundred cells that are multiplying quickly.");
        }
        else if(diff ==4){
            tvStatusmsg.setText("Current Status: Deep in your uterus, your baby is an embryo made up of two layers, and your primitive placenta is developing.");
        }
        else if(diff ==5){
            tvStatusmsg.setText("Current Status: Your tiny embryo is growing like crazy, and you may be noticing pregnancy discomforts like sore breasts and fatigue.");
        }
        else if(diff ==6){
            tvStatusmsg.setText("Current Status: Your baby's nose, mouth, and ears are beginning to take shape. You may be having morning sickness and spotting.");
        }
        else if(diff ==7){
            tvStatusmsg.setText("Current Status: Your baby – still an embryo with a small tail – is forming hands and feet. Your uterus has doubled in size.");
        }
        else if(diff ==8){
            tvStatusmsg.setText("Current Status: Your baby is constantly moving, though you can't feel it. Meanwhile, you may be making decisions about prenatal tests.");
        }
        else if(diff ==9){
            tvStatusmsg.setText("Current Status: Nearly an inch long now, your baby is starting to look more human. You've probably noticed your waist thickening.");
        }
        else if(diff ==10){
            tvStatusmsg.setText("Current Status: Your baby has finished the most critical part of development! Organs and structures are in place and ready to grow.");
        }
        else if(diff ==11){
            tvStatusmsg.setText("Current Status: Your baby's hands will soon open and close into fists, and tiny tooth buds are appearing underneath the gums.");
        }
        else if(diff ==12){
            tvStatusmsg.setText("Current Status: Your little one's teeny toes can curl, her brain is growing furiously, and her kidneys are starting to excrete urine.");
        }
        else if(diff ==13){
            tvStatusmsg.setText("Current Status: It's the last week of the first trimester! Your baby now has exquisite fingerprints and is almost 3 inches long.");
        }
        else if(diff ==14){
            tvStatusmsg.setText("Current Status: Your baby's tiny features are making different expressions. And you may be feeling more energetic and less nauseated.");
        }
        else if(diff ==15){
            tvStatusmsg.setText("Current Status: Your baby can sense light and is forming taste buds. Have a stuffy nose? It's a surprising pregnancy side effect.");
        }
        else if(diff ==16){
            tvStatusmsg.setText("Current Status: Get ready for a growth spurt. In the next few weeks, your baby will double his weight and add inches to his length.");
        }
        else if(diff ==17){
            tvStatusmsg.setText("Current Status: Your baby's skeleton is changing from soft cartilage to bone, and the umbilical cord is growing stronger and thicker.");
        }
        else if(diff ==18){
            tvStatusmsg.setText("Current Status: Your baby's genitals are developed enough to see on an ultrasound. Hungry? An increase in appetite is normal now.");
        }
        else if(diff ==19){
            tvStatusmsg.setText("Current Status: Go ahead and sing: Your baby may be able to hear you! And if your sides are aching, it could be round ligament pain.");
        }
        else if(diff ==20){
            tvStatusmsg.setText("Current Status: Congratulations, you're at the halfway mark in your pregnancy! Your baby is swallowing more now and producing meconium.");
        }
        else if(diff ==21){
            tvStatusmsg.setText("Current Status: Feeling your baby move? Those early flutters will turn into full-fledged kicks. Cool fact: She has eyebrows now!");
        }
        else if(diff ==22){
            tvStatusmsg.setText("Current Status: Your baby is starting to look like a miniature newborn. And your growing belly may be turning into a hand-magnet.");
        }
        else if(diff ==23){
            tvStatusmsg.setText("Current Status: When you're on the move, your baby can feel the motion. Pretty soon, you may notice swelling in your ankles and feet.");
        }
        else if(diff ==24){
            tvStatusmsg.setText("Current Status: Your baby is long and lean, like an ear of corn. And your growing uterus is now the size of a soccer ball.");
        }else if(diff ==25){
            tvStatusmsg.setText("Current Status: Your little one is starting to add some baby fat and grow more hair. Your hair may be looking extra lustrous, too.");
        }
        else if(diff ==26){
            tvStatusmsg.setText("Current Status: Your baby is inhaling and exhaling small amounts of amniotic fluid, which is good practice for breathing.");
        }
        else if(diff ==27){
            tvStatusmsg.setText("Current Status: Feel a tickle? It may be your baby hiccupping. He's also opening and closing his eyes and even sucking his fingers.");
        }
        else if(diff ==28){
            tvStatusmsg.setText("Current Status: Your developing baby's eyes may be able to see light filtering in through your womb.");
        }
        else if(diff ==29){
            tvStatusmsg.setText("Current Status: Your baby's muscles and lungs are continuing to mature, and her head is growing to make room for her developing brain.");
        }
        else if(diff ==30){
            tvStatusmsg.setText("Current Status: Your baby now weighs almost 3 pounds. Meanwhile, you may be battling mood swings, clumsiness, and fatigue.");
        }
        else if(diff ==31){
            tvStatusmsg.setText("Current Status: Your baby's strong kicks might be keeping you up at night – and you may be feeling Braxton Hicks contractions, too.");
        }
        else if(diff ==32){
            tvStatusmsg.setText("Current Status: Your baby is plumping up! Meanwhile, your expanding uterus may cause heartburn and shortness of breath.");
        }
        else if(diff ==33){
            tvStatusmsg.setText("Current Status: With your baby now weighing a little over 4 pounds, you might be waddling – and having trouble getting comfy in bed.");
        }
        else if(diff ==34){
            tvStatusmsg.setText("Current Status: Your baby's central nervous system and lungs are maturing, and dizziness and fatigue may be slowing you down.");
        }
        else if(diff ==35){
            tvStatusmsg.setText("Current Status: Your baby is too snug in your womb to do somersaults, but you'll still feel frequent – if less dramatic – movements.");
        }
        else if(diff ==36){
            tvStatusmsg.setText("Current Status: Your baby is gaining about an ounce a day. You may feel her \"drop\" down into your pelvis as you approach your due date.");
        }
        else if(diff ==37){
            tvStatusmsg.setText("Current Status: Your baby's brain and lungs are continuing to mature. You may have more vaginal discharge and occasional contractions.");
        }
        else if(diff ==38){
            tvStatusmsg.setText("Current Status: Your baby has a firm grasp, which you'll soon be able to test in person! Meanwhile, watch out for signs of preeclampsia.");
        }
        else if(diff ==39){
            tvStatusmsg.setText("Current Status: Your baby is full term this week and waiting to greet the world! If your water breaks, call your healthcare provider.");
        }
        else if(diff ==40){
            tvStatusmsg.setText("Current Status: Your baby is the size of a small pumpkin! Don't worry if you're still pregnant – it's common to go past your due date.");
        }
        else if(diff ==41){
            tvStatusmsg.setText("Current Status: As cozy as he is, your baby can't stay inside you much longer. You'll go into labor or be induced soon.");
        }

        else{
            statusView.removeAllViews();
        }


    }

    private void SendUserToSettingsActivity() {
        Intent settingsIntent= new Intent(MainActivity.this,settingsActivity.class);
        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(settingsIntent);
        finish();
    }

    private void LoginActivity() {
        Intent loginIntent= new Intent(MainActivity.this,loginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.menuCustomReminder){
            Intent queryIntent= new Intent(MainActivity.this,queryActivity.class);
            startActivity(queryIntent);
        }
        if(menuItem.getItemId() == R.id.menuRoutineVisit){
            Intent alarmNotificationIntent= new Intent(MainActivity.this,alarmNotification.class);
            startActivity(alarmNotificationIntent);
        }
        if(menuItem.getItemId() == R.id.menuSttings){
            Intent blogIntent= new Intent(MainActivity.this,blog_activity.class);
            startActivity(blogIntent);
        }
        if(menuItem.getItemId() == R.id.menuLogOut){
            mAuth.signOut();
            LoginActivity();
        }
        return false;
    }
}