// KWIC Index Production System

function parse(input) {
  var lines = [];
  var textLines = input.split("\n");
  for (var lineIndex = 0; lineIndex < textLines.length; lineIndex++) {
    lines[lineIndex] = textLines[lineIndex].split(" ");
  }
  return lines;
}

function shift(lines) {
  var permutations = [];
  for (var lineIndex = 0; lineIndex < lines.length; lineIndex++) {
    var line = lines[lineIndex];
    for (var wordIndex = 0; wordIndex < line.length; wordIndex++) {
      var left = line.slice(0, wordIndex);
      var right = line.slice(wordIndex, line.length);
      permutations.push(right.concat(left));
    }
  }
  return permutations;
}

function alphabetize(lines) {
  return lines.sort(function (line1, line2) {
    return line1[0].toLowerCase().localeCompare(line2[0].toLowerCase());
  });
}

function output(lines) {
  for (var lineIndex = 0; lineIndex < lines.length; lineIndex++) {
    console.log(lines[lineIndex].join(" "));
  }
}

var input = "This is the first line.\nAnd this is the second.";
output(alphabetize(shift(parse(input))));
