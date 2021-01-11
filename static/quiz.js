const startButton = document.getElementById('start-btn')
const nextButton = document.getElementById('next-btn')
const videoButton = document.getElementById('video-btn')
const nextVideo = document.getElementById('next-video')
const questionContainerElement = document.getElementById('question-container')
const questionElement = document.getElementById('question')
const answerButtonsElement = document.getElementById('answer-buttons')
const videoFrame = document.getElementById('videoframe')
const quizFrame = document.getElementById('quizframe')


let shuffledQuestions, currentQuestionIndex, currentVideoIndex, questions = [], videos = []

startButton.addEventListener('click', startGame)
nextButton.addEventListener('click', () => {
  currentQuestionIndex++
  setNextQuestion()
})

function startGame() {
  startButton.classList.add('hide')
  shuffledQuestions = questions.sort(() => Math.random() - .5)
  currentQuestionIndex = 0
  currentVideoIndex = 0
  questionContainerElement.classList.remove('hide')
  setNextQuestion()
}

function setNextQuestion() {
  resetState()
  showQuestion(shuffledQuestions[currentQuestionIndex])
}

function showQuestion(question) {
  questionElement.innerText = question.question
  question.answers.forEach(answer => {
    const button = document.createElement('button')
    button.innerText = answer.text
    button.classList.add('btn')
    if (answer.correct) {
      button.dataset.correct = answer.correct
    }
    button.addEventListener('click', selectAnswer)
    answerButtonsElement.appendChild(button)
  })
}

function resetState() {
  videoButton.classList.add('hide')
  clearStatusClass(document.body)
  nextButton.classList.add('hide')
  while (answerButtonsElement.firstChild) {
    answerButtonsElement.removeChild(answerButtonsElement.firstChild)
  }
}

function selectAnswer(e) {
  const selectedButton = e.target
  const correct = selectedButton.dataset.correct
  setStatusClass(document.body, correct)
  Array.from(answerButtonsElement.children).forEach(button => {
    setStatusClass(button, button.dataset.correct)
  })
  if (shuffledQuestions.length > currentQuestionIndex + 1) {
    nextButton.classList.remove('hide')
  } else {
    startButton.innerText = 'Restart'
    startButton.classList.remove('hide')
    videoButton.classList.remove('hide')
    videoButton.addEventListener('click', () => { videoFrame.classList.remove('hide'); quizFrame.classList.add('hide'), showVideo() })
  }
}

function setStatusClass(element, correct) {
  clearStatusClass(element)
  if (correct) {
    element.classList.add('correct')
  } else {
    element.classList.add('wrong')
  }
}

function showVideo() {

  nextButton.addEventListener('click', () => {
    currentQuestionIndex++
    setNextQuestion();
  })

  document.getElementById('test').src = 'https://www.youtube.com/embed/' + videos[currentVideoIndex]
  nextVideo.addEventListener('click', () => { 
    if (currentVideoIndex <= 4) {
      videos.forEach(document.getElementById('test').src = 'https://www.youtube.com/embed/' + videos[currentVideoIndex ++] ) }
    else {
      alert("Take a new quiz")
      document.location.replace('/all_quizzes');
    } 
  })
}


function clearStatusClass(element) {
  element.classList.remove('correct')
  element.classList.remove('wrong')
}

function populateQuiz(category) {
  console.log(`category: ${category}`);
  $.ajax({
    url: `http://localhost:4567/api/categories/${category}`,
    headers: {"Accept": "application/json"}
  })
  .done(function (data) { 
    questions = data.questions
    videos = data.videoLinks
    // Data is the response from the server.
    console.log(data);
    


    // Insert into quiz elements.

  })
  .fail(function (data) {
    // Do something when the call fails.
    console.log("call failed");
  });
}

