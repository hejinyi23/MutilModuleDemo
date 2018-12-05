# MutilModuleDemo
模块化、组件化，集成ButterKnife、Arouter、Zxing、RxJava2+retrofit2+OkHTTP3。解决资源冲突、Application冲突、模块间通讯、模块间跳转等问题
1、命名为xxLib属于功能型模块，只有实现某项具体功能的代码，如扫码、网络请求。不包含任何具体业务逻辑。
2、命名为xxModule的，除了baseModule都是业务型模块，在这里实现具体业务，如登录。
