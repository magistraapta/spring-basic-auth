import http from 'k6/http';
import { sleep, check } from 'k6';
import { SharedArray } from 'k6/data';
import encoding from 'k6/encoding';

// Test configuration
export const options = {
  stages: [
    { duration: '30s', target: 10 }, // Ramp up to 10 users
    { duration: '1m', target: 10 },  // Stay at 10 users
    { duration: '30s', target: 0 },  // Ramp down to 0 users
  ],
  thresholds: {
    http_req_duration: ['p(95)<500'], // 95% of requests must complete under 500ms
    http_req_failed: ['rate<0.01']    // less than 1% request failure rate
  }
};

// Test data
const testUser = {
  username: 'testuser',
  password: 'password123'
};

// Create Basic Auth header
const credentials = `${testUser.username}:${testUser.password}`;
const base64Credentials = encoding.b64encode(credentials);
const authHeader = `Basic ${base64Credentials}`;

// Common headers for all requests
const headers = {
  'Content-Type': 'application/json',
  'Authorization': authHeader
};

// Test scenario
export default function() {
  // 1. Get all products
  const productsRes = http.get('http://localhost:8080/products/', { headers });

  check(productsRes, {
    'products status is 200': (r) => r.status === 200,
    'products response time is acceptable': (r) => r.timings.duration < 500
  });

  sleep(1);

  // 2. Get specific product (if we have any)
  if (productsRes.status === 200 && productsRes.json().length > 0) {
    const firstProductId = productsRes.json()[0].id;
    const productRes = http.get(`http://localhost:8080/products/${firstProductId}`, { headers });

    check(productRes, {
      'product detail status is 200': (r) => r.status === 200,
      'product detail response time is acceptable': (r) => r.timings.duration < 500
    });
  }

  sleep(1);

  // 3. Get user's orders
  const ordersRes = http.get('http://localhost:8080/orders/my-orders', { headers });

  check(ordersRes, {
    'orders status is 200': (r) => r.status === 200,
    'orders response time is acceptable': (r) => r.timings.duration < 500
  });

  // Add a longer delay between iterations
  sleep(2);
}