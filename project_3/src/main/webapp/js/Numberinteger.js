function validateIntegerInput(event) {
  var char = event.which || event.keyCode;
  var input = event.target;
  
  // Allow only digits 0-9
  if (!(char >= 48 && char <= 57) && // 0-9
      char !== 8 && // backspace
      char !== 37 && // left arrow
      char !== 39 && // right arrow
      char !== 46) { // delete
    event.preventDefault();
    return false;
  }
  
  // Ensure the input length does not exceed 8 characters
  if (input.value.length >= 8 && !(char === 8 || char === 37 || char === 39 || char === 46)) {
    event.preventDefault();
    return false;
  }
}
