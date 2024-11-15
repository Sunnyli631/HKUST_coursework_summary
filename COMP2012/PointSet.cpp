#include <iostream>
#include "PointSet.h"
using namespace std;

PointSet::PointSet(int capacity)
{
  // This cout statement is for learning purpose only, which shows when the constructor will be invoked
  cout << "Initialized by PointSet's conversion constructor" << endl;

  // TODO #1: add your code here
  points = new Point[capacity];
  this->capacity = capacity;
  numPoints = 0;
}

PointSet::PointSet(const Point points[], int numPoints)
{
  // This cout statement is for learning purpose only, which shows when the constructor will be invoked
  cout << "Initialized by PointSet's other constructor" << endl;

  // TODO #2: add your code here
  this->points = new Point[numPoints];
  for(int i=0; i<numPoints; ++i)
    this->points[i] = points[i];
  capacity = numPoints;
  this->numPoints = numPoints;
}

PointSet::PointSet(const PointSet& s)
{
  // This cout statement is for learning purpose only, which shows when the constructor will be invoked
  cout << "Initialized by PointSet's copy constructor" << endl;

  // TODO #3: add your code here
  points = new Point[s.capacity];
  for(int i=0; i<s.capacity; ++i)
    points[i] = s.points[i];
  capacity = s.capacity;
  numPoints = s.numPoints;
}

PointSet::~PointSet()
{
  // TODO #4: add your code here
  delete [] points;
}

void PointSet::addPoint(const Point& p)
{
  // TODO #5: add your code here
  if(numPoints == capacity) {
    cout << "Insufficient array space" << endl;
  }
  else {
    points[numPoints++] = p;
  }
}

void PointSet::removeLastPoint()
{
  // TODO #6: add your code here
  if(numPoints == 0) {
    cout << "No points" << endl;
  } 
  else {
    --numPoints;
  }
}

// Return true if the all the points in the point set is collinear; return false otherwise
bool PointSet::isCollinear() const
{
  // TODO #7: add your code here
  for(int i = 2; i < numPoints; i++)
  {
    if(isCollinear_3points(points[i], points[i-1], points[i-2]) != true)
      return false;
  }
  return true;
}

// return true if 3 given points are on the same straight line
bool PointSet::isCollinear_3points(const Point p1, const Point p2, const Point p3) const
{
  // TODO #8: add your code here
  const double EPSILON = 0.01;
  double term1, term2, term3, sum;
  term1 = p1.getX() * (p2.getY() - p3.getY());
  term2 = p2.getX() * (p3.getY() - p1.getY());
  term3 = p3.getX() * (p1.getY() - p2.getY());
  sum = term1 + term2 + term3;
  return ( (sum * sum) < EPSILON );
}

void PointSet::print() const
{
  // This function is already completed.
  if (numPoints == 0) {
    cout << "The PointSet is empty!" << endl;
    return;
  }
  cout << "Number of points: " << numPoints << endl;;
  cout << "points: " << endl;
  for(int i = 0; i < numPoints; i++)
  {
    points[i].print();
    cout << endl;
  }
}
