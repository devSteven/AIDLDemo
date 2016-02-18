# AIDLDemo
 Android 进程间通信之AIDL例子
 
#前言
由于android系统中应用程序之间不能共享内存。因此，在不同应用程序之间交互数据（跨进程通讯）就稍微麻烦一些。

在android SDK中提供了4种用于跨进程通讯的方式。这4种方式正好对应于android系统中4种应用程序组件：Activity、Content Provider、Broadcast和Service。
 

 - Activity可以跨进程调用其他应用程序的Activity；
 - Content Provider可以跨进程访问其他应用程序中的数据（以Cursor对象形式返回），当然，也可以对其他应用程序的数据进行增、删、改操 作；
 - Broadcast可以向android系统中所有应用程序发送广播，而需要跨进程通讯的应用程序可以监听这些广播；
 - Service和Content Provider类似，也可以访问其他应用程序中的数据，但不同的是，Content Provider返回的是Cursor对象，
 - Service返回的是Java对象，这种可以跨进程通讯的服务叫AIDL服务。

#AIDL介绍

##AIDL 是什么
　　AIDL (Android Interface Definition Language) 是一种IDL 语言，用于生成可以在Android设备上两个进程之间进行进程间通信(interprocess communication, IPC)的代码。如果在一个进程中（例如Activity）要调用另一个进程中（例如Service）对象的操作，就可以使用AIDL生成可序列化的参数。
　　AIDL IPC机制是面向接口的，像COM或Corba一样，但是更加轻量级。它是使用代理类在客户端和实现端传递数据。
##AIDL 的作用
　　由于每个应用程序都运行在自己的进程空间，并且可以从应用程序UI运行另一个服务进程，而且经常会在不同的进程间传递对象。在Android平台，一个进程通常不能访问另一个进程的内存空间，所以要想对话，需要将对象分解成操作系统可以理解的基本单元，并且有序的通过进程边界。
　　通过代码来实现这个数据传输过程是冗长乏味的，Android提供了AIDL工具来处理这项工作。

##选择AIDL的使用场合
　　

> 官方文档特别提醒我们何时使用AIDL是必要的：只有你允许客户端从不同的应用程序为了进程间的通信而去访问你的service，以及想在你的service处理多线程。
> 　　如果不需要进行不同应用程序间的并发通信(IPC)，you should create your interface by
> implementing a Binder；或者你想进行IPC，但不需要处理多线程的，则implement your interface
> using a Messenger。无论如何，在使用AIDL前，必须要理解如何绑定service——bindService。
