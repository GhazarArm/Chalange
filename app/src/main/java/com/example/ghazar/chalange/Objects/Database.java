package com.example.ghazar.chalange.Objects;

import android.util.Log;

import com.example.ghazar.chalange.Activitys.MainActivity;
import com.example.ghazar.chalange.Activitys.WaitingChallengeRequestActivity;
import com.example.ghazar.chalange.FirstPage.FirstActivity;
import com.example.ghazar.chalange.HelperClases.Global;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Vector;

/**
 * Created by ghazar on 11/30/17.
 */

public class Database {

    public static String MY_ACCOUNT_DATABASE_NAME = "m_account";
    public static String MY_ACCOUNT_FRENDS_DATABASE_NAME = "m_frends";
    public static String MY_ACCOUNT_EVENTS_DATABASE_NAME = "m_events";

    public static String LEATERS_STORAGE_NAME = "Leaters";

    public static final String NAME = "_name";
    public static final String LAST_NAME = "_lastName";
    public static final String AGE = "_age";
    public static final String ID = "_id";
    public static final String GENDER = "_gender";


    public static DatabaseReference m_db = FirebaseDatabase.getInstance().getReference();
    public static FirebaseStorage   m_fStorage = FirebaseStorage.getInstance();
    public static StorageReference  m_storage = m_fStorage.getReference();
    public static StorageReference  m_leatersStorag = m_storage.child(LEATERS_STORAGE_NAME);

    public static DatabaseReference m_accountsDB = m_db.child("Accounts");
    public static DataSnapshot m_AccountDataSnapshot = null;

    public DatabaseReference m_accountInfoDB = null;
    public DataSnapshot m_AccountInfoDataSnapshot = null;

    public DatabaseReference m_accountFrendsDB = null;
    public DataSnapshot m_AccountFrendsDataSnapshot = null;

    public DatabaseReference m_accountEventsDB = null;
    public DataSnapshot m_AccountEventsDataSnapshot = null;

    public DatabaseReference m_GamesDB = m_db.child("Games");
    public DataSnapshot m_GamesDataSnapshot = null;

    public static String m_id = null;

    public Database() {
        m_accountsDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                m_AccountDataSnapshot = dataSnapshot;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(getActivity(), databaseError.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public Database(String id) {
        m_id = id;
        m_accountsDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                m_AccountDataSnapshot = dataSnapshot;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(getActivity(), databaseError.toString(), Toast.LENGTH_LONG).show();
            }
        });
        getCurentAccountDatabas();
    }

    public void setID(String id){
        m_id = id;
        getCurentAccountDatabas();
    }

    public void addGame(GameObject game){
        m_GamesDB.child(game.m_gameID).setValue(game);
    }

    public void deleteGame(GameObject game){
        m_GamesDB.child(game.m_gameID).removeValue();
    }

    public void sendMessage(String gameID, String text, int witchPlayer /*if witchPlayer == 1 => send firstPlayer if witchPlayer == 2 send second player*/){
        if(witchPlayer == 1)
            m_GamesDB.child(gameID).child(GameObject.PLAYER1_MESSAGE_KEY).setValue(text);
        else if(witchPlayer == 2)
            m_GamesDB.child(gameID).child(GameObject.PLAYER2_MESSAGE_KEY).setValue(text);
    }

    public void initDatabasesChangeEvent() {
        if (m_accountInfoDB != null) {
            m_accountInfoDB.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    m_AccountInfoDataSnapshot = dataSnapshot;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        if (m_accountFrendsDB != null) {
            m_accountFrendsDB.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    m_AccountFrendsDataSnapshot = dataSnapshot;
                    try{
                       MainActivity.m_mainActivity.initFrendsList();
                    }catch (NullPointerException ex){
                        Log.e("MY ERROR", ex.toString());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        if (m_accountEventsDB != null) {
            m_accountEventsDB.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    m_AccountEventsDataSnapshot = dataSnapshot;
                    int frendRequestCount = 0;
                    int guestRequestCount = 0;
                    int challangeRequestCount = 0;
                    int messageRequestCount = 0;
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        if(postSnapshot.child(Events.EVENT_KEY).getValue(String.class).equals(Events.FREND_REQUEST_EVENT_KEY))
                            ++frendRequestCount;
                        else if(postSnapshot.child(Events.EVENT_KEY).getValue(String.class).equals(Events.ACCOUNT_GUEST_KEY))
                            ++guestRequestCount;
                        else if(postSnapshot.child(Events.EVENT_KEY).getValue(String.class).equals(Events.CHALANGE_REQUEST_EVENT_KEY))
                            ++challangeRequestCount;
                        else if(postSnapshot.child(Events.EVENT_KEY).getValue(String.class).equals(Events.MESSAGE_REQUEST_EVENT_KEY))
                            ++messageRequestCount;
                        else if(postSnapshot.child(Events.EVENT_KEY).getValue(String.class).equals(Events.CHALANGE_ACCEPT_REQUEST_EVENT_KEY))
                            WaitingChallengeRequestActivity.m_this.challangeAcceptedBy(postSnapshot.child(Events.EVENT_TEXT).getValue(String.class));
                        else if(postSnapshot.child(Events.EVENT_KEY).getValue(String.class).equals(Events.CHALANGE_CANCEL_REQUEST_EVENT_KEY))
                            WaitingChallengeRequestActivity.m_this.challangeCanceledBy(postSnapshot.child(Events.EVENT_TEXT).getValue(String.class));
                    }
                    if(MainActivity.m_mainActivity != null){
                        MainActivity.m_mainActivity.setBadgeDrawableCount(frendRequestCount + guestRequestCount);
                        MainActivity.m_mainActivity.setFrendCounter(frendRequestCount);
                        MainActivity.m_mainActivity.setGuestCounter(guestRequestCount);
                    }

                    if(MainActivity.m_mainActivity != null){
                            MainActivity.m_mainActivity.setFabCount(challangeRequestCount);
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }
    }

    public Vector<String> getChallangeRequests(){
        Vector<String> vec = new Vector<String>();
        for(DataSnapshot postSnapshot : m_AccountEventsDataSnapshot.getChildren()) {
            if (postSnapshot.child(Events.EVENT_KEY).getValue(String.class).equals(Events.CHALANGE_REQUEST_EVENT_KEY)) {
                vec.add(postSnapshot.child(Events.EVENT_TEXT).getValue(String.class));
            }
        }
        return vec;
    }

    public int getChallangesRequestsCount(){
        int count = 0;
        for(DataSnapshot postSnapshot : m_AccountEventsDataSnapshot.getChildren()){
            if(postSnapshot.child(Events.EVENT_KEY).getValue(String.class).equals(Events.CHALANGE_REQUEST_EVENT_KEY)) {
                ++count;
            }
        }
        return count;
    }

    public void deleteEvent(String id, String key){
        for(DataSnapshot postSnapshot : m_AccountDataSnapshot.child(id).child(Database.MY_ACCOUNT_EVENTS_DATABASE_NAME).getChildren()){
            if(postSnapshot.child(Events.EVENT_KEY).getValue(String.class).equals(key)
                    && postSnapshot.child(Events.EVENT_TEXT).getValue(String.class).equals(FirstActivity.m_database.m_id))
            {
                postSnapshot.getRef().removeValue();
            }
        }
    }

    public void deleteMyEvents(String id, String key){
        for(DataSnapshot postSnapshot : m_AccountEventsDataSnapshot.getChildren()){
            if(postSnapshot.child(Events.EVENT_KEY).getValue(String.class).equals(key)
                    && postSnapshot.child(Events.EVENT_TEXT).getValue(String.class).equals(id))
            {
                postSnapshot.getRef().removeValue();
            }
        }
    }

    public void getCurentAccountDatabas() {
        for (DataSnapshot postSnapshot : m_AccountDataSnapshot.getChildren()) {
            try {
                String id = postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(ID).getValue(String.class);
                if (!id.equals(m_id))
                    continue;

                m_accountInfoDB = m_accountsDB.child(postSnapshot.getKey()).child(MY_ACCOUNT_DATABASE_NAME);
                m_accountFrendsDB = m_accountsDB.child(postSnapshot.getKey()).child(MY_ACCOUNT_FRENDS_DATABASE_NAME);
                m_accountEventsDB = m_accountsDB.child(postSnapshot.getKey()).child(MY_ACCOUNT_EVENTS_DATABASE_NAME);
                initDatabasesChangeEvent();
                break;
            } catch (NullPointerException ex) {
                Log.e("MY ERROR", ex.toString());
            }
        }
    }

    public void AddAccount(String name, String lastName, int age, boolean gander, String id)
    {
        Account acc = new Account(name, lastName, age, gander, id);
        Events events = new Events(Events.ACCOUNT_CREATED_EVENT_KEY, Events.ACCOUNT_CREATED_EVENT_TEXT);
        Frends frend = new Frends(id);
        frend.AddFrend("1949566748618430Facebook");
        AccountDB accountDB = new AccountDB(acc, events, frend);
        m_accountsDB.child(id).setValue(accountDB);
    }

    public Vector<Account> SearchAccount(String text, int maxAge, int minAge) {
        String[] words = Global.getWords(text);//get words of the String
        Vector<Account> accounts = new Vector<Account>();
        Vector<Account> accounts1 = new Vector<Account>();
        Vector<Account> accounts2 = new Vector<Account>();
        Vector<Account> accounts3 = new Vector<Account>();
        Vector<Account> accounts4 = new Vector<Account>();
        Vector<Account> accounts5 = new Vector<Account>();
        Vector<Account> accounts6 = new Vector<Account>();
        Vector<Account> accounts7 = new Vector<Account>();
        Vector<Account> accounts8 = new Vector<Account>();
        for (DataSnapshot postSnapshot : m_AccountDataSnapshot.getChildren()) {
            String firstName = postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(NAME).getValue(String.class);
            String lastName = postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(LAST_NAME).getValue(String.class);

            int category = 0;      //search result ordered white category. if category small resoult is important

            for(String str : words){
                if (firstName.toLowerCase().startsWith(words[0].toLowerCase())) {
                    category += 1;
                } else if (lastName.toLowerCase().startsWith(text.toLowerCase())) {
                    category += 2;
                } else if (firstName.toLowerCase().contains(text.toLowerCase())) {
                    category += 3;
                } else if (lastName.toLowerCase().contains(text.toLowerCase())) {
                    category += 4;
                }
            }
            Account acc = null;
            int age = postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(AGE).getValue(int.class);
            if (category != 0 && age >= minAge && age <= maxAge) {
                String id = postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(ID).getValue(String.class);
                boolean gender = postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(GENDER).getValue(boolean.class);
                acc = new Account(firstName, lastName, age, gender, id);
            }
            switch (category){
                case 0: break;
                case 1: accounts1.add(acc); break;
                case 2: accounts2.add(acc); break;
                case 3: accounts3.add(acc); break;
                case 4: accounts4.add(acc); break;
                case 5: accounts5.add(acc); break;
                case 6: accounts6.add(acc); break;
                case 7: accounts7.add(acc); break;
                default: accounts8.add(acc); break;
            }
        }
        for(Account a : accounts1)
            accounts.add(a);
        for(Account a : accounts2)
            accounts.add(a);
        for(Account a : accounts3)
            accounts.add(a);
        for(Account a : accounts4)
            accounts.add(a);
        if(accounts.size() >= 0)
            return accounts;

        return null;
    }

    public Vector<Account> getAllAccounts() {
        Vector<Account> accounts = new Vector<Account>();
        for (DataSnapshot postSnapshot : m_AccountDataSnapshot.getChildren()) {
            String name = postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(NAME).getValue(String.class);
            String lastName = postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(LAST_NAME).getValue(String.class);
            int age = postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(AGE).getValue(int.class);
            boolean gender = postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(GENDER).getValue(boolean.class);
            String id = postSnapshot.child(MY_ACCOUNT_DATABASE_NAME).child(ID).getValue(String.class);
            Account acc = new Account(name, lastName, age, gender, id);
            accounts.add(acc);
        }
        return accounts;
    }

    public Vector<String> getAllFrends() {
        Vector<String> frendsID = new Vector<String>();
        for (DataSnapshot postSnapshot : m_AccountFrendsDataSnapshot.child(Frends.FRENDS_VECTOR_KEY).getChildren()) {
            String id = postSnapshot.getValue(String.class);
            frendsID.add(id);
        }

        return frendsID;
    }

    public Account getAccount(String id) {
        try {
            DataSnapshot postSnapshot = m_AccountDataSnapshot.child(id).child(MY_ACCOUNT_DATABASE_NAME);
            String name = postSnapshot.child(NAME).getValue(String.class);
            String lastName = postSnapshot.child(LAST_NAME).getValue(String.class);
            int age = postSnapshot.child(AGE).getValue(int.class);
            boolean gender = postSnapshot.child(GENDER).getValue(boolean.class);
            return new Account(name, lastName, age, gender, id);
        }catch (Exception ex){
            return null;
        }
    }

    public boolean isAccountExist(String id) {
        return m_AccountDataSnapshot.child(id).exists();
    }

    //<<<<<<<<<<<<<<<<<<<<<<< Change Parameters <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    public void changeName(String name)
    {
        if(m_accountInfoDB != null)
        {
            m_accountInfoDB.child(NAME).setValue(name);
        }
    }

    public void changeLastName(String lastname)
    {
        if(m_accountInfoDB != null)
        {
            m_accountInfoDB.child(LAST_NAME).setValue(lastname);
        }
    }

    public void changeAge(int age)
    {
        if(m_accountInfoDB != null)
        {
            m_accountInfoDB.child(AGE).setValue(age);
        }
    }

    public void changeGender()
    {
        if(m_accountInfoDB != null)
        {
            m_accountInfoDB.child(GENDER).setValue(MainActivity.m_mainActivity.m_curentAccount.get_gender());
        }
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    public void sendFrendRequest(String idToAccount)
    {
        DatabaseReference toAccountEventsDatabase = m_accountsDB.child(idToAccount).child(MY_ACCOUNT_EVENTS_DATABASE_NAME);
        DataSnapshot toAccountDataSnapshot = m_AccountDataSnapshot.child(idToAccount).child(MY_ACCOUNT_EVENTS_DATABASE_NAME);
        for(DataSnapshot event : toAccountDataSnapshot.getChildren())
        {
            if(event.child(Events.EVENT_KEY).getValue(String.class).equals(Events.FREND_REQUEST_EVENT_KEY) &&
                    (event.child(Events.EVENT_TEXT).getValue(String.class).equals(m_id)))
                return;
        }
        Events event = new Events(Events.FREND_REQUEST_EVENT_KEY, m_id);
        toAccountEventsDatabase.push().setValue(event);
        return;
    }

    public void sendGuestRequest(String idToAccount)
    {
        DatabaseReference toAccountEventsDatabase = m_accountsDB.child(idToAccount).child(MY_ACCOUNT_EVENTS_DATABASE_NAME);
        Events event = new Events(Events.ACCOUNT_GUEST_KEY, m_id);
        toAccountEventsDatabase.push().setValue(event);
    }

    public void sendChallangeRequest(String id){
        DatabaseReference toAccountEventsDatabase = m_accountsDB.child(id).child(MY_ACCOUNT_EVENTS_DATABASE_NAME);
        Events event = new Events(Events.CHALANGE_REQUEST_EVENT_KEY, m_id);
        toAccountEventsDatabase.push().setValue(event);
    }

    public void sendChallangeAcceptRequest(String id){
        DatabaseReference toAccountEventsDatabase = m_accountsDB.child(id).child(MY_ACCOUNT_EVENTS_DATABASE_NAME);
        Events event = new Events(Events.CHALANGE_ACCEPT_REQUEST_EVENT_KEY, m_id);
        toAccountEventsDatabase.push().setValue(event);
    }

    public void sendChallangeCancelRequest(String id){
        DatabaseReference toAccountEventsDatabase = m_accountsDB.child(id).child(MY_ACCOUNT_EVENTS_DATABASE_NAME);
        Events event = new Events(Events.CHALANGE_CANCEL_REQUEST_EVENT_KEY, m_id);
        toAccountEventsDatabase.push().setValue(event);
    }

    public void addFrend(String id)
    {
        for(DataSnapshot postSnapshot : m_AccountFrendsDataSnapshot.child(Frends.FRENDS_VECTOR_KEY).getChildren())
        {
            if(postSnapshot.getValue(String.class).equals(id))
            {
                return;
            }
        }
        m_accountFrendsDB.child(Frends.FRENDS_VECTOR_KEY).push().setValue(id);
    }

    public void deleteFrend(String id)
    {
        for(DataSnapshot postSnapshot : m_AccountFrendsDataSnapshot.child(Frends.FRENDS_VECTOR_KEY).getChildren())
        {
            if(postSnapshot.getValue(String.class).equals(id))
            {
                postSnapshot.getRef().removeValue();
                break;
            }
        }
    }
}
