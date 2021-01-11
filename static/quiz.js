// The JavaScript-code for the Quiz and the vieos.
// @Author: Moa Svensson, Kristin Unger Berglund

// Getting several Ids from the HTML-document
const startButton = document.getElementById('start-btn')
const nextButton = document.getElementById('next-btn')
const videoButton = document.getElementById('video-btn')
const nextVideo = document.getElementById('next-video')
const questionContainerElement = document.getElementById('question-container')
const questionElement = document.getElementById('question')
const answerButtonsElement = document.getElementById('answer-buttons')
const videoFrame = document.getElementById('videoframe')
const quizFrame = document.getElementById('quizframe')

// Declares variables. Makes questions and videos to empty array.
let shuffledQuestions, currentQuestionIndex, currentVideoIndex, questions = [], videos = []

startButton.addEventListener('click', startGame)
nextButton.addEventListener('click', () => {
  currentQuestionIndex++
  setNextQuestion()
})

// Start game. Adds 'hide' to the start button, and shows the questions instead, obs random. 
// Sets currentQuestionIndex to 0, and currentVideoIndex to 0. Makes it possible to change that later.
// Run setNextQuestion()
function startGame() {
  startButton.classList.add('hide')
  shuffledQuestions = questions.sort(() => Math.random() - .5)
  currentQuestionIndex = 0
  currentVideoIndex = 0
  questionContainerElement.classList.remove('hide')
  setNextQuestion()
}

// Run resetState()
// Run showQuestion, sends the shuffeldQuestions with the currentQuestionIndex
function setNextQuestion() {
  resetState()
  showQuestion(shuffledQuestions[currentQuestionIndex])
}

// Show the questions
// Changes the text in the HTML-document, takes questions from question.question
// Takes each answer (loop) for that question and puts it in the HTML-document
// If the answer is correct we add a data atribute for that answer. Only do this for the right answers, 
// to make the checking of the answers easier.
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

// Function that resets the question and answers,
// Also showing the neccacary buttons
function resetState() {
  videoButton.classList.add('hide')
  clearStatusClass(document.body)
  nextButton.classList.add('hide')
  while (answerButtonsElement.firstChild) {
    answerButtonsElement.removeChild(answerButtonsElement.firstChild)
  }
}

// Eventlistener that takes the selected answer from showQuestion and checks if the answer is correct,
// by looping through the buttons (answers) with Array.from(answerButtonsElement.children).forEach.
// Function that shuffles throguh the questions until the quiz is over then show the restart
// and wacth video buttons and redirects to the function showVideo
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

// Function that sets class elements to correct and wrong
function setStatusClass(element, correct) {
  clearStatusClass(element)
  if (correct) {
    element.classList.add('correct')
  } else {
    element.classList.add('wrong')
  }
}

// Function that starts the game over if user pushes 'Restart' or start all of the videos, 
// one by one, then redirects to all_quizzes pages
function showVideo() {
  nextButton.addEventListener('click', () => {
    currentQuestionIndex++
    setNextQuestion();
  })

  // Gets the videoLind-Id from the HTML-documentation, and adds the URL for youtube, 
  // (together with the videoId from the API).
  // If the currentVideoIndex is more than 4 we redirect to /all_quizzes.
  // We do this because we only have 5 videos.
  document.getElementById('videolink').src = 'https://www.youtube.com/embed/' + videos[currentVideoIndex]
  nextVideo.addEventListener('click', () => { 
    if (currentVideoIndex <= 4) {
      videos.forEach(document.getElementById('videolink').src = 'https://www.youtube.com/embed/' + videos[currentVideoIndex ++] ) }
    else {
      alert("Take a new quiz")
      document.location.replace('/all_quizzes');
    } 
  })
}

// Function that removes the class elements correct & wrong
function clearStatusClass(element) {
  element.classList.remove('correct')
  element.classList.remove('wrong')
}

// Function that connects tp API server and populates questions and videoLinks given the different categories 
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
    

  })
  .fail(function (data) {
    // Do something when the call fails. This needs to be updated !OBS!
    console.log("call failed");
  });
}

