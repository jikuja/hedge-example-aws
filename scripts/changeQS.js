#!/usr/bin/env node

const fs = require('fs');
const os = require('os');
const path = require('path');
const process = require('process');
const qs = require('querystring');

const sep = path.sep;
const tmpDir = os.tmpdir();


function createOutputFile(input) {
  const tmpDir = fs.mkdtempSync(os.tmpdir());
  return path.join(tmpDir, input);
}

const inputFile = path.join(__dirname, 'local-invoke-plain.json');
const input = process.argv[2];
const data = JSON.parse(fs.readFileSync(inputFile));
const output = createOutputFile(path.basename(inputFile));
parsed = qs.parse(input);
data['queryStringParameters'] = parsed;
fs.writeFileSync(output, JSON.stringify(data))


console.log(output);
