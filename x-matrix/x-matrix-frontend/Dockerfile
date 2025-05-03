# Build Stage
FROM node:18 as build

WORKDIR /app
COPY package.json package-lock.json ./
RUN npm install
COPY . .
RUN npm run build

# Serve Stage (no Nginx)
FROM node:18

WORKDIR /app
RUN npm install -g serve
COPY --from=build /app/dist ./dist


EXPOSE 3000
CMD ["serve", "-s", "dist", "-l", "3000"]
