const startButton = document.getElementById('start-btn')
const nextButton = document.getElementById('next-btn')
const questionContainerElement = document.getElementById('question-container')
const questionElement = document.getElementById('question')
const answerButtonsElement = document.getElementById('answer-buttons')

let shuffledQuestions, currentQuestionIndex

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

function clearStatusClass(element) {
  element.classList.remove('correct')
  element.classList.remove('wrong')
}

const questions = [
  {
    question: 'Docker containers and images are included in Plesk Backup and migrated by Plesk Migrator?',
    answers: [
      { text: 'False', correct: true },
      { text: 'True', correct: false }
    ]
  },
  {
    question: 'How to login into docker hub?',
    answers: [
      { text: '$ docker $login', correct: false },
      { text: '$ docker --log', correct: false },
      { text: '$ docker login', correct: true },
    ]
  },
  {
    question: 'Docker hosts IP address by default is 192.168.99.100?',
    answers: [
      { text: 'True', correct: true },
      { text: 'False', correct: false },
    ]
  },
  {
    question: 'What Is K8s?',
    answers: [
      { text: 'Kubernetes, also sometimes called K8S (K – eight characters – S), is an open source backend framework for containerized applications that was born from the Azure data centers.', correct: false },
      { text: 'Kubernetes, also sometimes called K8S (K – eight characters – S), is an open source orchestration framework for containerized applications that was born from the Google data centers', correct: true }
    ]
  }
]