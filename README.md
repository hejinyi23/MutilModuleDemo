# MutilModuleDemo
模块化、组件化，集成ButterKnife、Arouter、EventBus,解决资源冲突、Application冲突等问题

1、loginModule与webModule属于业务类型模块
2、zxingLib与eventLib属于功能型模块
3、baseModule提供通用的地方三库、谷歌支持库、通用的工具类等通用性质的东西，所有模块都可以依赖于它。
4、eventLib仅用于定义各个Module间传递消息的EventBus事件。
