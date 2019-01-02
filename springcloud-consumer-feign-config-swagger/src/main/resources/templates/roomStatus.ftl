<#assign base = request.contextPath />
<!DOCTYPE html>
<html lang="zh">
  <body>
    <#include "/header.ftl">
    <!-- END header -->
    <section class="site-hero overlay semScroll" data-stellar-background-ratio="0.5" style="background-image: url(../images/big_image_1.png);">
      <div class="container">
            <div>
                <h1 class="text-center">房间状态</h1>
                <ul>
                    <#list roomList as rooms>
                        <li class="list-unstyled">
                            <#if rooms_index%2==0>
                            <span>
                                <img src="../images/room.png">
                            </span>
                            </#if>
                            <#if rooms_index%2==1>
                            <span>
                                <img src="../images/roomred.png">
                            </span>
                            </#if>
                            <span class="lableText" style="width:50px;">${rooms_index+101}</span>
                            </span>
                        </li>
                    </#list>
                </ul>
            </div>
      </div>
    </section>
    <!-- END section -->
    <#include "/footer.ftl">
  </body>
</html>