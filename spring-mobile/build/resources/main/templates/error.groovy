yieldUnescaped '<!DOCTYPE html>'
html(lang: 'en') {
    head {
        meta('http-equiv': '"Content-Type" content="text/html; charset=utf-8"')
        title('Courses Demo')
        link(rel: 'stylesheet', id: 'all-css-0', href: '/styles/main.css', type: 'text/css')
    }
    body {
        h1('Error')
        if (problem) div(class: 'error') { p(problem) }
        else {
            p('There was a problem')
        }
    }
}
