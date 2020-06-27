const { createProxyMiddleware } = require('http-proxy-middleware');
const URI = 'http://localhost:11078';
module.exports = function (app) {
    const apiProxy = createProxyMiddleware('/api', {target: URI});
    const wsProxy = createProxyMiddleware('/socket', {ws: true, target: URI});
    app.use(apiProxy);
    app.use(wsProxy);
};