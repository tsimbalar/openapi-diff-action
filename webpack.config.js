module.exports = {
  entry: './src/index.ts',
  target: 'node',
  output: {
    filename: 'bundle.js',
  },
  resolve: {
    extensions: ['.tsx', '.ts', '.js', '.json'],
  },
  module: {
    rules: [
      // all files with a '.ts' or '.tsx' extension will be handled by 'ts-loader'
      { test: /\.tsx?$/, use: ['ts-loader'] },
    ],
  },
  optimization: {
    sideEffects: false,
  },
};
