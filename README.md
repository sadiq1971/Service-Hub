Service-Hub
=============================
So far----

- [Authentication using firebase Database]
- [Get Location based Service Provider ]
- [Auto Suggested search Bar and implemented]
- [Get service Provider list from firebase real time database]


Data Model
-----------
We used Firebase Database. 

The database has two "root" nodes:

  * `users` - a list of `User` objects, keyed by user ID. So
    `/users/<ID>/email` is the email address of the user with id=`<ID>`.
  *`Profile`-keyed by user ID which contains location,skills,rating(if service Provider).

  Libaray used:
  
  *android.support:recyclerview
  *android.support:cardview
  *android.support:design
  *firebaseui:firebase-ui-database
  *google.firebase:firebase-auth
  *google.firebase:firebase-auth
  *github.florent37:materialtextfield
  *balysv:material-ripple
  *github.rey5137:material
  *jakewharton:butterknife
  *com.weiwangcn.betterspinner:library-material
  *cachapa.expandablelayout:expandablelayout
  
  
  
  =====================will be updated with more feature,very soon========================