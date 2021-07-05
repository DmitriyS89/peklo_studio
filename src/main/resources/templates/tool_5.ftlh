<#import "partials/main.ftlh" as main/>

<@main.renderWith>
    <h1 class="mt-4">Парсер подписчиков со страниц
        соцсетей
        Facebook, Instagram и ВКонтакте</h1>
    <div class="container-fluid">
        <p>*** В поле должна быть ссылка на юзера\группу выбранной соц-сети</p>
        <p>*** Ссылка должна выглядеть вот так:
        <ul>
            <li>
                <i>https://vk.com/user_id или https://vk.com/screen_name для пользователя</i>
            </li>
            <li>
                <i>https://m.vk.com/user_id или https://m.vk.com/screen_name для пользователя в мобильном режиме</i>
            </li>
            <li>
                <i>https://vk.com/group_id или https://vk.com/screen_name для группы</i>
            </li>
            <li>
                <i>https://m.vk.com/group_id или https://m.vk.com/screen_name для группы в мобильном режиме</i>
            </li>
        </ul>
        </p>
        <p>*** Ссылка не должна выглядеть вот так:
        <ul>
            <li><i>https://vk.com/some/some</i> Ссылка на другой путь соц-сети</li>
            <li><i>https://vk.com/id?some=some&some=some</i> Иметь лишние не нужные данные</li>
        </ul>
        </p>


        <form class="mx-auto w-50 d-flex flex-row" action="/tool_5/run" method="post">
            <div class="form-group">
                <input required type="text" class="form-control" name="urlFromFront" id="exampleFormControlInput1"
                       placeholder="domain">
            </div>
            <div class="form-group">
                <select class="form-control" name="social" id="exampleFormControlSelect1">
                    <option>vk</option>
                    <option disabled>facebook</option>
                    <option disabled>instagram</option>
                </select>
            </div>
            <div class="form-group">
                <select class="form-control" name="kind" id="exampleFormControlSelect1">
                    <option>user</option>
                    <option>group</option>
                </select>
            </div>
            <div class="form-group">
                <button type="submit" class="btn btn-secondary">Go</button>
            </div>
            <#if _csrf??>
                <input type="hidden" name="${(_csrf.parameterName)!'csrf'}" value="${(_csrf.token)!'--no-token--'}"/>
            </#if>
        </form>

        <div class="w-100 text-danger my-4">
            <#if success == "true">
                <div class="bg-warning px-2">
                    Успешно, результат отправлен на электронную почту.
                </div>
            <#elseif success = "userIsNull">
                <div class="bg-warning px-2">
                    Пользователь не найден!
                </div>
            <#elseif success = "groupIsNull">
                <div class="bg-warning px-2">
                    Группа не найдена!
                </div>
            <#elseif success = "urlNotCorrect">
                <div class="bg-warning px-2">
                    Неправильная ссылка!
                </div>
            <#elseif success = "false">
                <div class="bg-warning px-2">
                    ошибка
                </div>
            </#if>
        </div>
    </div>
</@main.renderWith>