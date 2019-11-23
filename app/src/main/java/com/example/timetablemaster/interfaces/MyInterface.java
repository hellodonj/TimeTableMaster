package com.example.timetablemaster.interfaces;

/**
 *  普通类MyInterface实现Interface1接口，必须实现所有的接口方法
 *  抽象类MyInterface实现Interface1接口,可以只实现方法method1
 */
public abstract class MyInterface implements Interface1{

    @Override
    public String method1() {
        return null;
    }
}

/**
 * 面向对象的三大特性：继承、封装、多态
 * 多态存在的三个必要条件：1、要有继承； 2、要有重写； 3、父类引用指向子类对象；
 * 数据类型：字节数：1     2     4     8             4      8        2     1
 *           整型： byte、short、int、long、浮点型：float、double、 char、boolean
 *           先整形，后浮点，最后特殊
 * Integer和int有什么区别
 * int是基本的数据类型，Integer是一个类，可以进行实例化出来一个对象，有自己的基本方法和属性；（Integer必须实例化后才能使用）
 * String、StringBuffer、StringBuilder 区别
 *     都是使用字符数组保存字符串；
 *     String是不可变的，因为有final修饰；StringBuffer、StringBuilder是可变的；
 *     String、StringBuffer是线程安全的；StringBuffer对方法加了同步锁；StringBuilder并没有对方法加同步锁，所以为非线程安全的。
 * 抽象类和接口的区别
 *     抽象类在Java语言中所表示的是一种继承关系，一个子类只能拥有一个父类，但可以实现多个接口；
 *     在抽象类中可以拥有自己的成员变量和非抽象方法，但是接口中只能存在静态不可变的成员数据，而他的方法都是抽象的。
 * 什么是内部类?
 *     定义：定义在另一类中的类。
 * 内部类为啥存在？
 *     内部类方法可以访问该类定义所在作用域的数据，包括被private修饰的私有数据
 *     内部类可以对同一包中的其他类隐藏起来
 *     内部类可以实现Java单继承的缺陷
 *     使用匿名内部类来实现简单的接口实现
 * 什么是值传递和引用传递
 * 请写出作用域public、private、protected、以及不写时（default或friendly）的区别
 *     (当前类——同一package——子类——其他包package)
 * equals和 "==" 的区别
 *     "=="针对基本数据类型，比较的是数据的值；针对对象，比较的是对象的地址，所以不同地址的对象都是false；
 *     equals 用来判断引用的对象是否一致，判断的是对象的内容是否相等
 * 进程和线程的区别
 *
 * HashMap和Hashtable的区别
 *
 * ArrayList和LinkedList的区别
 *
 * 重写（Override）和重载（Overload）的区别
 *     重写是对父类方法的重新实现（返回值、方法名、参数一致，方法内容重写；abstract和final除外）；
 *     重载是同一个方法名可以通过传递不同的参数类型和参数个数多次使用；（例如构造函数）
 *
 * 公司：
 * 企迈云商：150-500 QA
 * 网达软件：500以上 Android
 *
 */
