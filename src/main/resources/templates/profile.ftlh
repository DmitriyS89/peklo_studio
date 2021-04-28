<#import "partials/main.ftlh" as main/>

<@main.renderWith>
    <div class="container-fluid">

        <#if user ??>
            <table class="table w-50 mx-auto mt-3">
                <thead class="thead-light">
                <tr>
                    <th scope="col">Имя</th>
                    <th scope="col">email</th>
                    <th scope="col">Роль</th>
                    <th scope="col">Статус</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <th scope="row">${user.username}</th>
                    <td>${user.email}</td>
                    <td>${user.role}</td>
                    <td>${user.active}</td>
                </tr>
                </tbody>

            </table>
        </#if>

    </div>
</@main.renderWith>