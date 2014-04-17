package com.example.testaudioandroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.os.Handler;

public class NotificationCenter {

//---------------- event type list ---------------------

    public static enum NotificationID{
        DISCONNECT_SERVER,PLAYER_JOIN_ROOM,PLAYER_LEFT_ROOM,PLAYER_BEGIN_TALK,PLAYER_STOP_TALK
    } 


//---------------- singelton ---------------------------    

        private static NotificationCenter instance = null;

        private NotificationCenter() { observables = new HashMap<NotificationID, MyObservable>();   }

        public static synchronized NotificationCenter singelton() {
            if (instance == null) {
                        instance = new NotificationCenter ();
            }
            return instance;
        }

//-------------------------------------------

                public class Notification {

                    private Object poster;  // the object that post the event
                    private Object info;    // event specific data 
                    private NotificationID id;      // event name

                    public Notification(Object poster, NotificationID id, Object info) {
                        super();
                        this.poster = poster;
                        this.info = info;
                        this.id = id;
                    }

                    public Object getPoster() {
                        return poster;
                    }

                    public Object getInfo() {
                        return info;
                    }

                    public NotificationID getId() {
                        return id;
                    }
                }
        //-------------------------------------------

                public interface Notifiable {
                    public void onNotification(Notification notify);
                }

//-------------------------------------------

        protected class MyObservable {
            List<Notifiable> observers = new ArrayList<Notifiable>();

            public MyObservable() {
            }

            public void addObserver(Notifiable observer) {
                if (observer == null) {
                    throw new NullPointerException("observer == null");
                }
                synchronized (this) {
                    if (!observers.contains(observer))
                        observers.add(observer);
                }
            }

            public int countObservers() {
                return observers.size();
            }

            public synchronized void deleteObserver(Notifiable observer) {
                observers.remove(observer);
            }

            public synchronized void deleteObservers() {
                observers.clear();
            }

            public void notifyObservers(Notification notify) {
                int size = 0;
                Notifiable[] arrays = null;
                synchronized (this) {
                        size = observers.size();
                        arrays = new Notifiable[size];
                        observers.toArray(arrays);
                }
                if (arrays != null) {
                    for (Notifiable observer : arrays) {
                        observer.onNotification(notify);
                    }
                }
            }
        }

//-------------------------------------------

        HashMap<NotificationID, MyObservable > observables;

        public void addObserver(NotificationID id, Notifiable observer) {
            MyObservable observable = observables.get(id);
            if (observable==null) {
                observable = new MyObservable ();
                observables.put(id, observable);
            }
            observable.addObserver(observer);
        }

        public void removeObserver(NotificationID id, Notifiable observer) {
            MyObservable observable = observables.get(id);
            if (observable!=null) {         
                observable.deleteObserver(observer);
            }
        }

        public void removeObserver(Notifiable observer) {
             for (MyObservable observable : observables.values()) {
                 if (observable!=null) {         
                    observable.deleteObserver(observer);
                }    
            }
        }

        public void postNotification(final Object notificationPoster, final NotificationID id, final Object notificationInfo) {

            final MyObservable  observable = observables.get(id);
            if (observable!=null) {

                // notification post to the maim (UI) thread    
                // Get a handler that can be used to post to the main thread
                Handler mainHandler = new Handler(MyApplication.getInstance().getApplicationContext().getMainLooper());

                Runnable myRunnable = new Runnable() {

                    @Override
                    public void run() {
                        observable.notifyObservers(new Notification(notificationPoster, id, notificationInfo) );
                    }
                };

                mainHandler.post(myRunnable);
            }
        }
}