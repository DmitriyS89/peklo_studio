document.addEventListener('DOMContentLoaded', function () {

    var pass1 = document.querySelector('#pswd1'),

        pass2 = document.querySelector('#pswd2')

    pass1.addEventListener('input', function () {

        this.value !== pass2.value ? pass2.setCustomValidity('Passwords does not match') : pass2.setCustomValidity('')

    })

    pass2.addEventListener('input', function (e) {

        this.value !== pass1.value ? this.setCustomValidity('Password incorrect') : this.setCustomValidity('')

    })

})
