<#assign base = request.contextPath />
<!DOCTYPE html>
<html lang="zh">

  <body>
    <#include "/header.ftl">
    <!-- END header -->
    <section class="site-hero overlay" data-stellar-background-ratio="0.5" style="background-image: url(../images/big_image_1.png);">
      <div class="container">
        <div class="row align-items-center site-hero-inner justify-content-center">
          <div class="col-md-6">
              <h1>昂宝酒店</h1>
              <form action="#" method="post">
                  <div class="row">
                      <div class="col-md-6 form-group">
                          <label for="userName" class="lableText">用户名</label>
                          <input type="text" id="userName" name="userName" class="form-control ">
                      </div>
                  </div>
                  <div class="row">
                      <div class="col-md-6 form-group">
                          <label for="password" class="lableText">密码</label>
                          <input type="text" id="password" name="password" class="form-control ">
                      </div>
                  </div>
              </form>
              <div class="row">
                  <div class="col-md-6 form-group">
                      <a href="booknow.html" class="btn btn-primary form-control">登陆</a>
                  </div>
              </div>
          </div>
        </div>
    </section>
    <!-- END section -->
    <#include "/footer.ftl">
  </body>
  <script>
      $().ready(function () {
          $("#hotalName").hide();
          $("#fontPage").hide();
          $("#roomPage").hide();
          $("#setupPage").hide();
      });
  </script>
</html>