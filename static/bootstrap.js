var dropdownElementList = [].slice.call(document.querySelectorAll('.dropdown-toggle'))
var dropdownList = dropdownElementList.map(function (dropdownToggleEl) {
  return new bootstrap.Dropdown(dropdownToggleEl)
})

var myDropdown = document.getElementById('myDropdown')
myDropdown.addEventListener('show.bs.dropdown', function () {
  // do something...
})