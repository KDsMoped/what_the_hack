const jsonmarkdown = require('json-markdown');
const schemaLoc = './missions.json';
const outputFile = 'output-file.md'; // if you don't provide an output file, it will create an .md from your schema filename. 
// the third argument defines, if you want to write to file or not. Set it to false, if you dont need to create a markdown yet. 
jsonmarkdown.process(schemaLoc, outputFile, true, (err, result) => {
  console.log(result);
});