<#import "partials/main.ftlh" as main/>

<@main.renderWith>
    <h1 class="mt-4">Подтверждения Telegram аккаунта</h1>
    <div class="container-fluid">
        <p>*** There will be information of instrument</p>
        <p>*** There will be information of instrument</p>
        <p>*** There will be information of instrument</p>

        <div>
            <label class="d-flex"> Ссылка на telegram бота
                <input id="btn1_" type="text" class="w-100" name="telegram-link" readonly value="http://t.me/peklo_tool_1_bot">
                <button type="button" class="btn btn-light" onclick="myFunction1()">copy</button>
            </label>
            <label class="d-flex"> Ваш токен для подтверждения аккаунта
                <input id="btn2_" type="text" class="w-100" name="telegram-accept-token" readonly
                       value="${userToken}">
                <button type="button" class="btn btn-light" onclick="myFunction2()">copy</button>
            </label>
            <form action="/telegram-bot/new-token-for-user" method="post">
                <input type="hidden" name="userId" value="${userId}">
                <button type="submit" class="btn btn-dark w-100">Новый токен</button>
                <#if _csrf??>
                    <input type="hidden" name="${(_csrf.parameterName)!'csrf'}"
                           value="${(_csrf.token)!'--no-token--'}"/>
                </#if>
            </form>
        </div>
    </div>

    <div class="w-100 text-danger my-4">
        <#if tokenAccept == "true">
            <div class="bg-warning px-2">
                Успешно, аккаунт подтверждён!
            </div>
        <#elseif tokenAccept = "false">
            <div class="bg-warning px-2">
                Ошибка!
            </div>
        <#elseif tokenAccept = "again">
            <div class="bg-warning px-2">
                Попробуйте занаво!
            </div>
        </#if>
    </div>
    <script type="text/javascript">
        function myFunction1() {
            let copyText = document.getElementById("btn1_");
            copyText.select();
            document.execCommand("copy");
        }
        function myFunction2() {
            let copyText = document.getElementById("btn2_");
            copyText.select();
            document.execCommand("copy");
        }
    </script>
</@main.renderWith>