yieldUnescaped '<!DOCTYPE html>'
html(lang:'en') {                                                                   
    head {                                                                          
        meta('http-equiv':'"Content-Type" content: "text/html; charset: utf-8"')      
        title('Courses Demo')
        link(rel: 'stylesheet', id: 'all-css-0', href: '/styles/main.css', type: 'text/css')
    }
    body {
        h3('Normal Home page')
        div(class: 'site_pref') {
            a(href: '/?site_preference=mobile', 'Mobile')
            yieldUnescaped '|'
            a(href: '/?site_preference=normal', 'Desktop')
        }
        div(class: 'content') {
            div {
                a(href: '/courses', 'Courses')
            }
        }
    }                                                                               
}
