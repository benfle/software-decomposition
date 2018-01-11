// KWIC Index System
//
// The KWIC index system accepts an ordered set of lines,
// each line is an ordered set of words, and each word is
// an ordered set of characters. Any line may be "circularly
// shifted" by repeatedly removing the first word and
// appending it at the end of the line. The KWIC index
// system outputs a listing of all circular shifts of all
// lines in alphabetical order.
//

// Text Storage

function Text() {
  this.lines = [];
}

(function() {

  this.lineAt = function(lineIndex) {
    return this.lines[lineIndex];
  },

  this.wordAt = function(lineIndex, wordIndex) {
    return this.lines[lineIndex][wordIndex];
  },

  this.putWordAt = function(lineIndex, wordIndex, aWord) {
    if (this.lines[lineIndex] === undefined) this.lines[lineIndex] = [];
    this.lines[lineIndex][wordIndex] = aWord;
  },

  this.lineCount = function() {
    return this.lines.length;
  },

  this.wordCount = function(lineIndex) {
    return this.lines[lineIndex].length;
  }

}).call(Text.prototype);

// Parser: String -> Text

function parse(input) {
  var text = new Text();
  var lineIndex = wordIndex = 0;
  input.split('\n').forEach(function(aLine) {
    wordIndex = 0;
    aLine.split(' ').forEach(function(aWord) {
      text.putWordAt(lineIndex, wordIndex, aWord);
      wordIndex++;
    });
    lineIndex++;
  });
  return text;
}

// Circular Shifter: Text -> Text (with circular shifts)

function CircularShifter(aText) {
  this.lines = [];
  for (var lineIndex = 0; lineIndex < aText.lineCount(); lineIndex++) {
    var line = aText.lineAt(lineIndex);
    for (var wordIndex = 0; wordIndex < line.length; wordIndex++) {
      var left = line.slice(0, wordIndex);
      var right = line.slice(wordIndex, line.length);
      this.lines.push(right.concat(left));
    }
  }
}

CircularShifter.prototype = new Text();

// Alphabetizer: Text -> Text (Alphabetically sorted)

function Alphabetizer(aText) {
  var lines = [];
  for (var lineIndex = 0; lineIndex < aText.lineCount(); lineIndex++) {
    lines.push([aText.wordAt(lineIndex, 0),
                aText.lineAt(lineIndex)]);
  }
  var sorted_lines = lines.sort(function(line1, line2) {
    return line1[0].toLowerCase().localeCompare(line2[0].toLowerCase());
  });
  this.lines = sorted_lines.map(function(l) { return l[1]; });
}

Alphabetizer.prototype = new Text();

// Output: Text -> String

function output(aText) {
  var string = "";
  for (var lineIndex = 0; lineIndex < aText.lineCount(); lineIndex++) {
    for (var wordIndex = 0; wordIndex < aText.wordCount(lineIndex); wordIndex++) {
      string += aText.wordAt(lineIndex, wordIndex);
      string += " ";
    }
    string += "\n";
  }
  return string;
}

var input = "This is the first line.\nAnd this is the second.";
console.log(output(new Alphabetizer(new CircularShifter(parse(input)))));
