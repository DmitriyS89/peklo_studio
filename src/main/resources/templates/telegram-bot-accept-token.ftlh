<#import "partials/main.ftlh" as main/>

<@main.renderWith>
    <h1 class="mt-4">Подтверждения Telegram аккаунта</h1>
    <div class="container-fluid">
        <p>*** There will be information of instrument</p>
        <p>*** There will be information of instrument</p>
        <p>*** There will be information of instrument</p>

        <div>
            <label class="d-flex"> Ссылка на telegram бота
                <input type="text" class="w-100" name="telegram-link" readonly value="http://t.me/peklo_tool_1_bot">
                <button type="button" class="btn btn-light">copy</button>
            </label>
            <label class="d-flex"> Ваш токен для подтверждения аккаунта
                <input type="text" class="w-100" name="telegram-accept-token" readonly
                       value="${userToken}">
                <button type="button" class="btn btn-light">copy</button>
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
</@main.renderWith>