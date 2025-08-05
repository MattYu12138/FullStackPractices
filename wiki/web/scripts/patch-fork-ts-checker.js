const fs = require('fs');
const path = require('path');

const filePath = path.resolve(__dirname, '../node_modules/fork-ts-checker-webpack-plugin-v5/lib/typescript-reporter/profile/TypeScriptPerformance.js');

try {
  if (fs.existsSync(filePath)) {
    let content = fs.readFileSync(filePath, 'utf8');
    if (content.includes('const { mark, measure } = typeScriptPerformance;') && !content.includes('markDescriptor')) {
      content = content.replace(
        'const { mark, measure } = typeScriptPerformance;\n        const { enable, disable } = performance;\n        typeScriptPerformance.mark = (name) => {\n            mark(name);\n            performance.mark(name);\n        };\n        typeScriptPerformance.measure = (name, startMark, endMark) => {\n            measure(name, startMark, endMark);\n            performance.measure(name, startMark, endMark);\n        };',
        'const { mark, measure } = typeScriptPerformance;\n        const { enable, disable } = performance;\n        const markDescriptor = Object.getOwnPropertyDescriptor(typeScriptPerformance, \'mark\');\n        const measureDescriptor = Object.getOwnPropertyDescriptor(typeScriptPerformance, \'measure\');\n        if (markDescriptor && markDescriptor.writable) {\n            typeScriptPerformance.mark = (name) => {\n                mark(name);\n                performance.mark(name);\n            };\n        }\n        if (measureDescriptor && measureDescriptor.writable) {\n            typeScriptPerformance.measure = (name, startMark, endMark) => {\n                measure(name, startMark, endMark);\n                performance.measure(name, startMark, endMark);\n            };\n        }'
      );
      fs.writeFileSync(filePath, content, 'utf8');
      console.log('Patched fork-ts-checker-webpack-plugin TypeScriptPerformance.');
    } else {
      console.log('TypeScriptPerformance already patched or unexpected content.');
    }
  }
} catch (err) {
  console.error('Failed to patch fork-ts-checker-webpack-plugin', err);
}
