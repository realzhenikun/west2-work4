# west2-work4
# 项目结构
  -activity  Activity
  
  -adapter  适配器
  
  -api-article  文章的接口
  
      -user  用户的接口
      
  -myclass-dataclass  数据类
  
          -helper  封装的部分使用频繁的接口调用方法
          
          AuthInterceptor  拦截器
          
  -service 封装Retrofit对象的creat方法
  
  App 构建全局变量token

图片资源放在res/drawable中

布局文件在res/layout

-----------------------------

# 各Activity功能
**LoginActivity**
用户注册和登录的界面，用户可以在此界面注册并登录

**MainActivity**
软件首页，可以从该界面跳转到文章详情界面、写文章界面以及我的主页界面

**WriteActivity**
写文章界面

**MyActivity**
我的主页，可以看到个人信息，我写的文章和我点赞过的文章，并且可以跳转到资料修改界面

**SetActivity**
资料修改界面，可以修改头像，名字，密码和简介

**UpdateActivity**
修改数据界面，输入修改后的数据

**ArticleActivity**
文章详情页，显示文章详细信息，可以点赞收藏评论文章

-----------------------------
考核各要求都已实现，但部分功能存在一定bug
