from bottle import route, run, template, request, static_file, redirect
from os import listdir

@route("/static/<file_name>")
def static_files(file_name):
    return static_file(file_name, root="static")

@route("/")
def list_articles():
    return template("index")

@route('/index')
def return_home():
    return template("index")

@route('/creators')
def aboutus_page():
    return template("creators")

@route('/contact')
def contact_page():
    return template("contact")

@route('/documentation')
def dokument_page():
    return template("documentation")

@route('/html')
def html_quiz_page():
    return template("Quiz", name='html')

@route('/php')
def php_quiz_page():
    return template("Quiz", name='php')

@route('/javascript')
def javascript():
    return template("Quiz", name='javascript')

@route('/wordpress')
def wordpress_quiz_page():
    return template("Quiz", name='wordpress')

@route('/sign-in')
def signin():
    return template("sign-in")

@route('/sign-up')
def signup():
    return template("sign-up")

@route('/video')
def html_video_page():
    return template("video", name='html')

@route('/video')
def php_video_page():
    print("HEJSAN")
    return template("video", name='php')

@route('/video')
def javascript_video():
    return template("video", name='javascript')

@route('/video')
def wordpress_video_page():
    return template("video", name='wordpress')


run(host='localhost', port=8086, debug=True, reloader=True)
