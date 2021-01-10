const startButton = document.getElementById('start-btn')
const nextButton = document.getElementById('next-btn')
const videoButton = document.getElementById('video-btn')
const questionContainerElement = document.getElementById('question-container')
const questionElement = document.getElementById('question')
const answerButtonsElement = document.getElementById('answer-buttons')
const videoFrame = document.getElementById('videoframe')
const quizFrame = document.getElementById('quizframe')


let shuffledQuestions, currentQuestionIndex, questions = []

startButton.addEventListener('click', startGame)
nextButton.addEventListener('click', () => {
  currentQuestionIndex++
  setNextQuestion()
})

function startGame() {
  startButton.classList.add('hide')
  shuffledQuestions = questions.sort(() => Math.random() - .5)
  currentQuestionIndex = 0
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
    videoButton.addEventListener('click', () => { videoFrame.classList.remove('hide'); quizFrame.classList.add('hide'), hejhopp() })
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

function hejhopp() {
  nextButton.addEventListener('click', () => {
    currentQuestionIndex++
    setNextQuestion()
  })
  document.getElementById('test').src = 'https://www.youtube.com/embed/' + videoID
  document.getElementById('test1').src = 'https://www.youtube.com/embed/' + videoID1
  document.getElementById('test2').src = 'https://www.youtube.com/embed/' + videoID2
  document.getElementById('test3').src = 'https://www.youtube.com/embed/' + videoID3
  document.getElementById('test4').src = 'https://www.youtube.com/embed/' + videoID4

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
    // Data is the response from the server.
    console.log(data);
    
    videoID = data.videoLinks[0]
    videoID1 = data.videoLinks[1]
    videoID2 = data.videoLinks[2]
    videoID3 = data.videoLinks[3]
    videoID4 = data.videoLinks[4]


    // Insert into quiz elements.

  })
  .fail(function (data) {
    // Do something when the call fails.
    console.log("call failed");
  });
}

