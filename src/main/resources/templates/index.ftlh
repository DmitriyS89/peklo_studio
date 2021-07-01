<#import "partials/main.ftlh" as main/>

<@main.renderWith>
    <div class="card my-3">
        <div class="card-header">
            Добро пожаловать!
        </div>
        <div class="card-body">
            <h5 class="card-title">Немного о сайте</h5>
            <p class="card-text">
                Сайт был разработан для маркетологов в образовательных целях.
            </p>
            <blockquote class="blockquote my-2">
                <p>Контакты заказчика:</p>
                <footer class="blockquote-footer">Сайт: <cite title="Ссылка на сайт заказчика"><a target="_blank" href="https://peklo.studio/ru">Peklo</a></cite></footer>
                <footer class="blockquote-footer">Адрес: <cite title="Электронный адрес заказчика"><a href="mailto:go@peklo.studio">go@peklo.studio</a></cite></footer>
                <footer class="blockquote-footer">Номер: <cite title="Номер телефона заказчика"><a href="tel:+996700997779">+996 700 997 779</a></cite></footer>
            </blockquote>

            <blockquote class="blockquote my-2">
                <p>Разработчики сайта:</p>
                <footer class="blockquote-footer">Attractor school: <h4 class="d-inline"><span class="text-white badge bg-secondary">Team 5</span></h4></footer>
            </blockquote>
        </div>
    </div>
</@main.renderWith>